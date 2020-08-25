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

@router.get("/{pkg_manager}/{product}/deps/{timestamp}")
async def rebuild_dependency_net(pkg_manager: str, product: str, timestamp: int, transitive: Optional[bool] = False):
    """
    Given a product and a timestamp, reconstruct its dependency network

    Return: A set of revisions, along with an adjacency matrix

    REST examples:

        GET /api/mvn/org.slf4j:slf4j-api/deps/1233323123
        GET /api/pypi/numpy/deps/1233323123?transitive=true
    """
    if transitive:
        t = "true"
        result = f"You have inputted the following data: ({pkg_manager}, {product}, {timestamp}, {t})."
    else:
        result = f"You have inputted the following data: ({pkg_manager}, {product}, {timestamp})."

    return {"message": result, "pkg_manager": pkg_manager, "product": product, "timestamp": timestamp, "transitive": transitive}


@router.get("/{pkg_manager}/{product}/cg/{timestamp}")
async def get_call_graph(pkg_manager: str, product: str, timestamp: int, transitive: Optional[bool] = False):
    """
    Given a product and a timestamp, retrieve its call graph

    Use case: A user wants to run a custom analysis locally.

    Return: A JSON-serialized RevisionCallGraph

    REST examples:

        GET /api/mvn/org.slf4j:slf4j-api/cg/1233323123
        GET /api/pypi/numpy/cg/1233323123?transitive=true
    """
    if transitive:
        t = "true"
        result = f"You have inputted the following data: ({pkg_manager}, {product}, {timestamp}, {t})."
    else:
        result = f"You have inputted the following data: ({pkg_manager}, {product}, {timestamp})."

    return {"message": result, "pkg_manager": pkg_manager, "product": product, "timestamp": timestamp, "transitive": transitive}


@router.get("/{pkg_manager}/{product}/{version}")
async def get_metadata(pkg_manager: str, product: str, version: str):
    """
    Given a product and a version, retrieve all known metadata

    Return: All known metadata for a revision

    REST examples:

        GET /api/mvn/org.slf4j:slf4j-api/1.7.29
        GET /api/pypi/numpy/1.15.2
    """
    result = f"You have inputted the following data: ({pkg_manager}, {product}, {version})."

    return {"message": result, "pkg_manager": pkg_manager, "product": product, "version": version}


@router.get("/{pkg_manager}/{product}/{version}/vulnerabilities")
async def get_vulnerabilities(pkg_manager: str, product: str, version: str):
    """
    Vulnerabilities in the transitive closure of a package version

    Expected result, in order of detail

    - Paths of revisions,
    - Paths of files / compilation units,
    - Paths of functions

    REST examples:

        GET /api/mvn/org.slf4j:slf4j-api/1.7.29/vulnerabilities
        GET /api/pypi/numpy/1.15.2/vulnerabilities
    """
    result = f"You have inputted the following data: ({pkg_manager}, {product}, {version})."
    
    # TODO: depends on the vulnerabilities plugin and data.

    return {"message": result, "pkg_manager": pkg_manager, "product": product, "version": version}


@router.post("/{pkg_manager}/{product}/{version}/impact")
async def post_(pkg_manager: str, product: str, version: str, transitive: Optional[bool] = False):
    """
    Impact analysis

    Use case: the user asks the KB to compute the impact of a semantic change
    to a function

    Exapected result: The full set of functions reachable from the provided
    function

    REST examples:

        POST /api/mvn/org.slf4j:slf4j-api/1.7.29/impact
        POST /api/mvn/org.slf4j:slf4j-api/1.7.29/impact?transitive=true

    The post body contains a FASTEN URI
    """
    # TODO: depends on the Impact analysis plugin and data (and need to clarify this use case)

    if transitive:
        t = "true"
        result = f"You have inputted the following data: ({pkg_manager}, {product}, {version}, {t})."
    else:
        result = f"You have inputted the following data: ({pkg_manager}, {product}, {version})."

    return {"message": result, "pkg_manager": pkg_manager, "product": product, "version": version, "transitive": transitive}


@router.post("/{pkg_manager}/{product}/{version}/cg")
async def update_cg(pkg_manager: str, product: str, version: str):
    """
    Update the static CG of a package version with new edges

    Use case: A user runs an instrumented test suite locally and decides to
    update the central call graph with edges that do not exist due to
    shortcomings in static analysis.

    Expected result: A list of edges that where added.

    REST examples:

        POST /api/mvn/org.slf4j:slf4j-api/1.7.29/cg
        POST /api/pypi/numpy/1.15.2/cg
    """
    # TODO: need to clarify this use case, understand where the data is stored.
    
    result = f"You have inputted the following data: ({pkg_manager}, {product}, {version})."
    
    return {"message": result, "pkg_manager": pkg_manager, "product": product, "version": version}
