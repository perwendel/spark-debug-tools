Zepto(function ($) {

    var $frameContainer = $('.frames-container');
    var $detailsContainer = $('.details-container');
    var $activeLine = $frameContainer.find('.frame.active');
    var $activeFrame = $detailsContainer.find('.frame-code.active');
    var $ajaxEditors = $('.editor-link[data-ajax]');

    prettyPrint();
    highlightCurrentLine();

    setTimeout(function() {
        $("#star-frame").attr("src", "https://ghbtns.com/github-btn.html?user=perwendel&repo=spark&type=star&count=true").css("width", "95px");
    }, 1000);

    function highlightCurrentLine () {
        // Highlight the active and neighboring lines for this frame:
        var activeLineNumber = +($activeLine.find('.frame-line').text());
        var $lines = $activeFrame.find('.linenums li');
        var firstLine = +($lines.first().val());

        $($lines[activeLineNumber - firstLine - 1]).addClass('current');
        $($lines[activeLineNumber - firstLine]).addClass('current active');
        $($lines[activeLineNumber - firstLine + 1]).addClass('current');
    }

    $frameContainer.on('click', '.frame', function () {
        var $this = $(this);
        setActiveFrame($this);
    });

    function setActiveFrame($this) {
        var id = /frame\-line\-([\d]*)/.exec($this.attr('id'))[1];
        var $codeFrame = $('#frame-code-' + id);

        if ($codeFrame) {
            $activeLine.removeClass('active');
            $activeFrame.removeClass('active');

            $this.addClass('active');
            $codeFrame.addClass('active');

            $activeLine = $this;
            $activeFrame = $codeFrame;

            highlightCurrentLine();

            $detailsContainer.scrollTop(0);
        }
    }

    var clipboard = new Clipboard('.clipboard');
    var showTooltip = function (elem, msg) {
        elem.setAttribute('class', 'clipboard tooltipped tooltipped-s');
        elem.setAttribute('aria-label', msg);
    };

    clipboard.on('success', function (e) {
        e.clearSelection();
        showTooltip(e.trigger, 'Full stacktrace copied to clipboard!');
    });

    clipboard.on('error', function (e) {
        showTooltip(e.trigger, fallbackMessage(e.action));
    });

    $('.clipboard').mouseleave(function (e) {
        e.currentTarget.setAttribute('class', 'clipboard');
        e.currentTarget.removeAttribute('aria-label');
    });

    function fallbackMessage(action) {
        var actionMsg = '';
        var actionKey = (action === 'cut' ? 'X' : 'C');

        if (/Mac/i.test(navigator.userAgent)) {
            actionMsg = 'Press ⌘-' + actionKey + ' to ' + action;
        } else {
            actionMsg = 'Press Ctrl-' + actionKey + ' to ' + action;
        }

        return actionMsg;
    }

    $("#google-button").click(function (e) {
        var exception = e.currentTarget.getAttribute("data-google-query");
        window.open("https://www.google.com/?#q=" + exception);
    });

    $(document).on('keydown', function (e) {
        if (e.ctrlKey) {
            // CTRL+Arrow-UP/Arrow-Down support:
            // 1) select the next/prev element
            // 2) make sure the newly selected element is within the view-scope
            // 3) focus the (right) container, so arrow-up/down (without ctrl) scroll the details
            if (e.which === 38 /* arrow up */) {
                $activeLine.prev('.frame').click();
                $activeLine[0].scrollIntoView();
                $detailsContainer.focus();
                e.preventDefault();
            } else if (e.which === 40 /* arrow down */) {
                $activeLine.next('.frame').click();
                $activeLine[0].scrollIntoView();
                $detailsContainer.focus();
                e.preventDefault();
            }
        }
    });

    // Avoid to quit the page with some protocol (e.g. IntelliJ Platform REST API)
    $ajaxEditors.on('click', function (e) {
        e.preventDefault();
        $.get(this.href);
    });

    // Set the first frame for which a code snippet could be found (if any) to be visible.
    // That frame will most likely be the most immediately relevant to the user.
    $(document).ready(function() {
      var codeFrames = $('.frame.has-code');

      if (codeFrames.length > 0) {
        setActiveFrame($(codeFrames[0]));
      }
    });
});
