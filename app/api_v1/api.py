from fastapi import APIRouter
from api_v1.endpoints import kafka

api_router = APIRouter()
api_router.include_router(kafka.router, tags=["kafka"])
