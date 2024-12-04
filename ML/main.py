from fastapi import FastAPI, HTTPException
from sklearn.feature_extraction.text import TfidfVectorizer
from bson import ObjectId
import pymongo
import pymysql
import uvicorn

app = FastAPI()

# Connect to MongoDB
client = pymongo.MongoClient("mongodb+srv://Cluster61823:25882588@cluster61823.ppkuv.mongodb.net/?retryWrites=true&w=majority&appName=Cluster61823")
db = client["OOD_CW"]
articles_collection = db["articles"]

mysql_connection = pymysql.connect(
    host="localhost",
    user="root",
    password="chira@root",
    database="OOD_CW"
)

def extract_keywords(content, num_keywords=10):
    vectorizer = TfidfVectorizer(max_features=num_keywords, stop_words='english')
    X = vectorizer.fit_transform([content]) 
    keywords = vectorizer.get_feature_names_out()  
    
    return list(keywords)


@app.post("/recommend")
def recommend_articles(preferences: dict):
    email = preferences['email']
    user_prefs = preferences['preferences']
    user_prefs = user_prefs.split(',')
    
    with mysql_connection.cursor() as cursor:
        cursor.execute("""
            SELECT article_id, interaction_type
            FROM history
            WHERE email = %s
        """, (email,))
        interactions = cursor.fetchall()
        
    preferences_profile = {}
    
    # Process historical interactions
    for article_id, interaction_type in interactions:
        weight = 2 if interaction_type == 'like' else 1 if interaction_type == 'view' else -1 if interaction_type == 'skip' else -2
        article = articles_collection.find_one({"_id": ObjectId(article_id)})
        if article:
            for keyword in article["keywords"]:
                preferences_profile[keyword] = preferences_profile.get(keyword, 0) + weight

    # Boost keywords from explicit preferences
    for keyword in user_prefs:
        preferences_profile[keyword] = preferences_profile.get(keyword, 0) + 3  # Higher weight for explicit preferences

    # Recommend articles based on combined profile
    recommended_articles = articles_collection.find(
        {"keywords": {"$in": list(preferences_profile.keys())}},
        {"_id": 1, "title": 1, "author": 1, "content": 1}
    )
    
    articles = []
    for article in recommended_articles:
        articles.append({
            "id": str(article["_id"]),
            "title": article['title'],
            "author": article['author'],
            "content": article['content']
        })

    if not articles:
        raise HTTPException(status_code=404, detail="No articles found for the given preferences")

    return {"recommended_articles": articles}


@app.post("/add_or_update_article")
async def add_or_update_article(article: dict):
    # Extract article data
    article_id = article.get("id")
    title = article.get("title")
    author = article.get("author")
    content = article.get("content")
    
    # Extract keywords from content
    keywords = extract_keywords(content)

    if article_id:
        # If an ID is provided, update the existing article
        result = articles_collection.update_one(
            {"_id": ObjectId(article_id)},  # Match article by ID
            {
                "$set": {
                    "title": title,
                    "author": author,
                    "content": content,
                    "keywords": keywords  # Update keywords
                }
            }
        )
        
        if result.matched_count == 0:
            raise HTTPException(status_code=404, detail="Article not found")

        return {"message": True}
    else:
        # If no ID is provided, insert a new article
        new_article = {
            "title": title,
            "author": author,
            "content": content,
            "keywords": keywords
        }
        result = articles_collection.insert_one(new_article)

        return {"id": str(result.inserted_id), "message": True}

if __name__ == "__main__":
    uvicorn.run(app, port=8001)
# uvicorn main:app --reload
