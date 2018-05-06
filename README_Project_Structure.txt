This project is a coding assessment.  The purpose is as follows:

User story
1.    Given a third party photo album web service which I have uploaded some pictures
2.    When I launch the app's landing page
3.    Then it should see a list of all my pictures I uploaded on the 3rd party service

The backend service is in Java Spring.  It has 2 endpoints: "getImages" and "getImagesForce".

getImages takes a startId (by REST param) and returns a list of up to 10 images from the images
repository beginning with that startId.  It will stop searching for images once it has searched
100 consecutive IDs without finding any images, and the response will contain whether or not
it was able to find 10 images, as well as the ID of the last image found (if any), or the last
id searched (if not all 10 images were found).  The values for the list size (10) and the ID
timeout (100) are configurable in application.properties.

getImagesForce is for use when the user knows there are more images to find but they are more
than 100 IDs removed from being consecutive.  This operation takes a start ID (by REST param)
and will search all possible IDs starting with the given start point for any image it finds.
It will return only a single image, and also return the ID of that image.  If this operation is
run in error (without any image to find), it is intended to run forever, so it should not be
run unless the user is sure it will not run forever.

The backend service structure is a simple Spring Boot RESTful microservice.  The main work is
done by the PictureService, which contains only a single method, which takes an ID and checks
if that ID exists on the remote third-party service; if it does, it returns the URL of the
image, otherwise returns null.  It is expected that the third-party service formats its data
correctly (i.e. all things marked as URLs are actually valid URLs); only extremely cursory
validation of the URL is performed.

The PictureListService contains 2 methods, one for each microservice.  The first method takes
a startId and generates the URL list described above by calling PictureService.  The second
method is used by for getPictureForced method and will iterate on PictureService forever
until it finds a picture.  The PictureListService passes its result to the PictureListController,
whose only function is to provide the REST endpoints and does no other work.

The frontend is a simple jQuery application.  On page load, it will load a batch of pictures.
As the page is scrolled, it will query the backend to load more pictures and load those pictures
onto the page dynamically using Javascript AJAX.  When no more pictures are found, a button
will appear which will allow the user to call getImagesForced, which will call the force service.
To ensure the button is not clicked in error, a confirm dialog will appear before the force service
is called.

There is a concurrency lock placed on the getData function in the jQuery code, as the scrolling
mechanism of JavaScript could cause getData to be called multiple times in succession, which
would cause the same images to be loaded multiple times.  To prevent this, concurrency locking
was implemented.

Regarding business logic uncertainties, the following problems were identified and handled:

1) The third-party service URL may change over time.  It is defined dynamically in
application.properties to allow the endpoint to change without having to recompile and redeploy
the whole application.  The requirement is that there is only a single parameter, which is an
integer ID, somewhere in the third-party endpoint URI.

2) The values 10 and 100 for list size and timeout may not be effective.  Again, they were
defined in application.properties to allow easy reconfiguration.

3) Since the number of images is unknown, and it is unknown whether some images may have been
deleted, it is difficult to determine when the service has determined no images exist, when
more images actually do exist.  It was determined that any solution to this problem has the
potential to run forever.  As a result, it was determined that such a solution should not be
implemented as the default service operation, and a heuristic of not finding a new image for a
"long" time (defined in this case as 100 IDs) would be preferable.  However, the solution of
running forever was implemented as a triggerable option by the user in case it's necessary.

4) The application does not have authentication.  Authentication would be useful for this service
in a production environment (to separate users from accessing each others' pictures), but since
the remote third-party API has no authentication, and no authentication was mentioned in the
assignment, none was implemented.