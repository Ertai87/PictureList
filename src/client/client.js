$(function(){

    var nextFileIndex = 0;
    var moreImages = true;
    var forceButtonActive = false;
    var getDataLock = false;

    var getData = function(){
        if (!getDataLock){
            getDataLock = true;
            $.ajax({
                type: "GET",
                url: "/tomcat/getImages/" + nextFileIndex + "/",
                error: function(xhr, status, error) {
                    alert("ERROR: " + JSON.parse(xhr.responseText).message);
                },
                success: function(data, status){
                    moreImages = data.full;
                    nextFileIndex = data.maxId + 1;
                    getImages(data.urls);
                    if (!moreImages && !forceButtonActive){
                        forceButtonActive = true;
                        $("#body").append("<button id=\"more\" onclick=\"$(this).forceGetData();\">Force More Images</button>");
                    }
                }
            });
            getDataLock = false;
        }
    };

    var getImages = function(urlsList){
        urlsList.forEach(function(url){
            $("#body").append("<img src=" + url + " style=\"width: 25%; height: 25%; border: 5px solid white;\">");
        });
    };

    (function($) {
        $.fn.forceGetData = function() {
            if(confirm("This operation is unsafe.  Are you sure there are more pictures available?")){
                $("#more").remove();
                forceButtonActive = false;
                $.ajax({
                    type: "GET",
                    url: "/tomcat/getImagesForce/" + nextFileIndex + "/",
                    error: function(xhr, status, error) {
                        alert("ERROR: " + JSON.parse(xhr.responseText).message);
                    },
                    success: function(data, status){
                        moreImages = data.full;
                        getImages(data.urls);
                        nextFileIndex = data.maxId + 1;
                    }
                });
            }
        };
    })(jQuery);

    $(window).scroll(function() {
        if($(window).scrollTop() + $(window).height() == $(document).height() && moreImages && !getDataLock) {
            getData();
        }
    });

    getData();
});