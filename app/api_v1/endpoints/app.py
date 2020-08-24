from typing import Optional
from fastapi import APIRouter
# from api_v1.endpoints.utils import connect_mddb
# , HTTPException

router = APIRouter()
# TODO: implement model (if necessary)
# from pydantic import BaseModel
# class Item(BaseModel):
#     name: str
#     price: float
#     is_offer: bool = None

# @router.put("/items/{item_id}")
# async def update_item(item_id: int, item: Item):
#     return {"item_name": item.name, "item_id": item_id}

# TODO: implement response model
# @router.get("/", response_model=List[Item])


@router.get("/")
async def root():
    return {"message": "it's working!"}
