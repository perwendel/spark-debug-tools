$(document).ready(function() {
    var exceptions = $(".frames-container");

    /* Go through each exception... */
    for (var i = 0; i < exceptions.length; i++) {
        var exception = $(exceptions[i]);
        var frames = exception.find(".frame");
        var length = 0;

        /* Require at least two frames. */
        if (frames.length >= 2) {
            /* Collapse consecutive frames with no code. 
               First and last frame always should be visible. */
            for (var j = 1; j < frames.length - 1; j++) {
                var frame = $(frames[j]);

                if (!frame.hasClass("has-code")) {
                    frame.addClass("shrunk");
                    length += 1;
                }

                if (frame.hasClass("has-code") || j == frames.length - 2) {
                    if (length > 1) {
                        frame.before('<div class="expander">Show ' + (length) + ' empty frames</div>');
                        length = 0;
                    } else if (length == 1) {
                        $(frames[j - 1]).removeClass("shrunk");
                    }
                }
            }
        }
    }

    $(".expander").click(function(event) {
        var element = $(this).prev("div");

        while (element.length > 0) {
            if (!element.hasClass("shrunk")) {
                break;
            }

            element.removeClass("shrunk");
            element = $(element).prev("div");
        }

        element = $(this).next("div");

        while (element.length > 0) {
            if (!element.hasClass("shrunk")) {
                break;
            }

            element.removeClass("shrunk");
            element = $(element).next("div");
        }

        $(this).remove();
    });

    exceptions.css("display", "block"); 
});