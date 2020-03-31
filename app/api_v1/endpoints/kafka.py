from fastapi import APIRouter
from api_v1.endpoints.utils import connect_kafka

router = APIRouter()


@router.get("/")
async def root():
    return {"message": "it's working!"}


@router.get("/{pkg_manager}/{product}/deps/{timestamp}")
def rebuild_dependency_net(pkg_manager: str, product: str, timestamp: int):
    """
    Given a product and a timestamp, reconstruct its dependency network

    Return: A set of revisions, along with an adjacency matrix

    REST examples:

        GET /api/mvn/org.slf4j:slf4j-api/deps/1233323123
        GET /api/pypi/numpy/deps/1233323123?transitive=true
    """
    msg = connect_kafka()
    # TODO: send a kafka topic requesting a set of revisions
    # TODO: read the kafka topic with the answer
    # TODO: transform the data from kafka (if necessary)
    # TODO: return the set of revisions
    return msg


@router.get("/{pkg_manager}/{product}/cg/{timestamp}")
def get_call_graph():
    """
    Given a product and a timestamp, retrieve its call graph

    Use case: A user wants to run a custom analysis locally.

    Return: A JSON-serialized RevisionCallGraph

    REST examples:

        GET /api/mvn/org.slf4j:slf4j-api/cg/1233323123
        GET /api/pypi/numpy/cg/1233323123?transitive=true
    """
    pass


@router.get("/{pkg_manager}/{product}/{version}")
def get_metadata():
    """
    Given a product and a version, retrieve all known metadata

    Return: All known metadata for a revision

    REST examples:

        GET /api/mvn/org.slf4j:slf4j-api/1.7.29
        GET /api/pypi/numpy/1.15.2
    """
    pass


@router.get("/{pkg_manager}/{product}/{version}/vulnerabilities")
def get_vulnerabilities():
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
    pass


@router.post("/{pkg_manager}/{product}/{version}/impact")
def post_():
    """
    Impact analysis

    Use case: the user asks the KB to compute the impact of a semantic change
    to a function

    Exapected result: The full set of functions reachable from the provided
    function

    REST examples:

        POST /api/mvn/org.slf4j:slf4j-api/1.7.29/impact
        POST /api/mvn/org.slf4j:slf4j-api/1.7.29/impact?transitive

    The post body contains a FASTEN URI
    """
    # TODO: need to clarify this use case
    pass


@router.post("/{pkg_manager}/{product}/{version}/cg")
def update_cg():
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
    pass
