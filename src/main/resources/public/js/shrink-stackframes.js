$(".frame").addClass("tryShrink");

$("body").on("click", ".expander", function () {
    var groupId = $(this).attr("id");
    $(this).remove();
    $("." + groupId).removeClass("shrunk");
});

// If you're reading this... I'm sorry.
(function shrinkingTime(runsLeft) {
    var tryShrink = $(".frame.tryShrink");
    var beginIndex = 0;
    var endIndex = tryShrink.length;
    for (var i = 0; i < tryShrink.length; i++) {
        if (!$(tryShrink[i]).hasClass("has-code")) {
            beginIndex = i;
            break;
        }
    }
    for (var i = 0; i < tryShrink.length; i++) {
        if (i > beginIndex && $(tryShrink[i]).hasClass("has-code")) {
            endIndex = i;
            break;
        }
    }
    var shrink = tryShrink.slice(beginIndex, endIndex);
    var groupId = "g" + $(shrink[0]).find(".frame-index").text() + "-" + $(shrink[shrink.length - 1]).find(".frame-index").text();
    for (var i = 0; i < shrink.length; i++) {
        $(shrink[i]).removeClass("tryShrink");
        if (shrink.length > 3) {
            if (i == beginIndex) {
                $(shrink[i]).after('<div id="' + groupId + '" class="expander">Show ' + (shrink.length - 2) + ' empty frames</div>');
            }
            if (i > 0 && i < shrink.length - 1) {
                $(shrink[i]).addClass("shrunk " + groupId);
            }
        }
    }
    tryShrink = $(".frame.tryShrink");
    for (var i = 0; i < tryShrink.length; i++) {
        if (!$(tryShrink[i]).hasClass("has-code") && runsLeft > 0) {
            shrinkingTime(runsLeft - 1);
            break;
        }
    }
})(10); //max runs
