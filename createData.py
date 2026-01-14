import requests
import random
import string

BASE_URL = "http://localhost:9090/api"

users = [f"user{i}" for i in range(1, 11)]
password = "password123"

posts_data = [
    ("Understanding REST APIs", "A" * 200),
    ("Spring Boot Basics", "B" * 200),
    ("JWT Authentication", "C" * 200),
    ("Database Indexing", "D" * 200),
    ("Microservices Architecture", "E" * 200),
    ("Concurrency in Java", "F" * 200),
    ("Python vs Java", "G" * 200),
    ("Docker Fundamentals", "H" * 200),
    ("Kubernetes Overview", "I" * 200),
    ("System Design Intro", "J" * 200),
    ("Clean Code Principles", "K" * 200),
    ("API Security", "L" * 200),
    ("Cloud Computing", "M" * 200),
    ("Scalable Backends", "N" * 200),
    ("Caching Strategies", "O" * 200),
    ("Load Balancing", "P" * 200),
    ("Message Queues", "Q" * 200),
    ("Event Driven Systems", "R" * 200),
    ("CI/CD Pipelines", "S" * 200),
    ("Monitoring and Logging", "T" * 200),
    ("Distributed Systems", "U" * 200),
    ("Latency Optimization", "V" * 200),
    ("Fault Tolerance", "W" * 200),
    ("Horizontal Scaling", "X" * 200),
    ("Vertical Scaling", "Y" * 200)
]

for username in users:
    requests.post(
        f"{BASE_URL}/users",
        json={
            "username": username,
            "password": password
        }
    )

for title, description in posts_data:
    requests.post(
        f"{BASE_URL}/posts",
        json={
            "title": title,
            "description": description,
            "username": random.choice(users)
        }
    )
