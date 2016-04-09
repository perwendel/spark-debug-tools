<#escape x as x?html>
<!DOCTYPE html>
<html>
<head>
    <title>${basic_type}</title>
    <meta charset="utf-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="theme-color" content="#CC2222">
    <meta name="apple-mobile-web-app-status-bar-style" content="#CC2222">
    <style type="text/css"><#include "/public/sparkdebugtools/css/sparkdebugtools_main.css"></style>
    <style type="text/css"><#include "/public/sparkdebugtools/css/sparkdebugtools_code.css"></style>
    <style type="text/css"><#include "/public/sparkdebugtools/css/sparkdebugtools_exception-buttons.css"></style>
</head>
<body>
<div id="spark-header">
    <h1>Spark Framework Debug</h1>
    <ul>
        <li><a href="http://sparkjava.com/documentation.html" target="_blank">View Documentation</a></li>
        <li><a href="https://sparktutorials.github.io/" target="_blank">View Tutorials</a></li>
    </ul>
    <div id="some-buttons">
        <a id="some-twitter" class="some-button" href="https://twitter.com/sparkjava" target="_blank"><img src="data:image/svg+xml;base64,PHN2ZyB4bWxuczpzdmc9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIiB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHZlcnNpb249IjEiIHg9IjAiIHk9IjAiIHdpZHRoPSIyMDAiIGhlaWdodD0iMjAwIiB2aWV3Qm94PSI0MCAtNjAgMjAwIDIwMCIgZW5hYmxlLWJhY2tncm91bmQ9Im5ldyA0MCAtNTkuOCAyMDAgMjAwIiB4bWw6c3BhY2U9InByZXNlcnZlIj48cGF0aCBmaWxsPSIjMkFBOUUwIiBkPSJNMTQwLTUwYzUwIDAgOTAgNDAgOTAgOTBzLTQwIDkwLTkwIDkwYy01MCAwLTkwLTQwLTkwLTkwUzkwLTUwIDE0MC01ME0xNDAtNjBDODUtNjAgNDAtMTUgNDAgNDBzNDUgMTAwIDEwMCAxMDBjNTUgMCAxMDAtNDUgMTAwLTEwMFMxOTUtNjAgMTQwLTYwTDE0MC02MHpNMTk5IDljLTQgMi05IDMtMTQgNCA1LTMgOS04IDExLTEzIC01IDMtMTAgNS0xNSA2IC00LTUtMTEtOC0xOC04IC0xMyAwLTI0IDExLTI0IDI0IDAgMiAwIDQgMSA2IC0yMC0xLTM4LTExLTUwLTI1IC0yIDQtMyA4LTMgMTIgMCA4IDQgMTYgMTEgMjAgLTQgMC04LTEtMTEtMyAwIDAgMCAwIDAgMCAwIDEyIDggMjIgMTkgMjQgLTIgMS00IDEtNiAxIC0yIDAtMyAwLTUgMCAzIDEwIDEyIDE3IDIzIDE3IC04IDctMTkgMTAtMzAgMTAgLTIgMC00IDAtNiAwIDExIDcgMjQgMTEgMzcgMTEgNDUgMCA2OS0zNyA2OS02OSAwLTEgMC0yIDAtM0MxOTIgMTggMTk2IDE0IDE5OSA5TDE5OSA5eiIvPjwvc3ZnPg==" title="Follow Spark on Twitter" alt="Follow Spark on Twitter"></a>
        <a id="some-gplus" class="some-button" href="https://plus.google.com/+Sparkjavaplus" target="_blank"><img src="data:image/svg+xml;base64,PHN2ZyB4bWxuczpzdmc9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIiB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHZlcnNpb249IjEiIHg9IjAiIHk9IjAiIHdpZHRoPSIyMDAiIGhlaWdodD0iMjAwIiB2aWV3Qm94PSItOSAtOSAyMDAgMjAwIiBlbmFibGUtYmFja2dyb3VuZD0ibmV3IC05LjUgLTkuMiAyMDAgMjAwIiB4bWw6c3BhY2U9InByZXNlcnZlIj48cGF0aCBmaWxsPSIjREM0QTM4IiBkPSJNOTEtOWMtNTUgMC0xMDAgNDUtMTAwIDEwMCAwIDU1IDQ1IDEwMCAxMDAgMTAwczEwMC00NSAxMDAtMTAwQzE5MSAzNiAxNDYtOSA5MS05TDkxLTl6TTkxIDFjNTAgMCA5MCA0MCA5MCA5MCAwIDUwLTQwIDkwLTkwIDkwIC01MCAwLTkwLTQwLTkwLTkwQzEgNDEgNDEgMSA5MSAxTTEwNCAxMDVsLTUtNGMtMi0xLTQtMy00LTYgMC0zIDItNSA0LTcgNi01IDEzLTEwIDEzLTIxIDAtMTEtNy0xNy0xMS0yMGg5bDEwLTZIOTBjLTggMC0yMCAyLTI4IDkgLTYgNi05IDEzLTkgMjAgMCAxMiA5IDIzIDI1IDIzIDIgMCAzIDAgNSAwIC0xIDItMSAzLTEgNiAwIDUgMiA4IDUgMTAgLTcgMS0xOSAxLTI4IDcgLTkgNS0xMSAxMy0xMSAxOCAwIDExIDEwIDIxIDMyIDIxIDI2IDAgMzktMTQgMzktMjhDMTE2IDExNiAxMTAgMTEwIDEwNCAxMDV6TTg0IDg4Yy0xMyAwLTE5LTE3LTE5LTI3IDAtNCAxLTggMy0xMSAyLTMgNy01IDEwLTVDOTIgNDUgOTggNjIgOTggNzNjMCAzIDAgOC00IDExQzkyIDg2IDg4IDg4IDg0IDg4ek04NCAxNDhjLTE2IDAtMjYtOC0yNi0xOCAwLTExIDEwLTE0IDEzLTE1IDYtMiAxNC0yIDE2LTIgMiAwIDIgMCAzIDAgMTEgOCAxNiAxMiAxNiAyMEMxMDYgMTQxIDk5IDE0OCA4NCAxNDh6Ii8+PHBvbHlnb24gZmlsbD0iI0RDNEEzOCIgcG9pbnRzPSIxNDEgODggMTQxIDczIDEzNCA3MyAxMzQgODggMTE5IDg4IDExOSA5NSAxMzQgOTUgMTM0IDExMCAxNDEgMTEwIDE0MSA5NSAxNTYgOTUgMTU2IDg4ICIvPjwvc3ZnPg==" title="Follow Spark on Google Plus" alt="Follow Spark on Google Plus"></a>
        <a id="some-facebook" class="some-button" href="https://www.facebook.com/sparkjava" target="_blank"><img src="data:image/svg+xml;base64,PHN2ZyB4bWxuczpzdmc9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIiB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHZlcnNpb249IjEuMSIgeD0iMCIgeT0iMCIgd2lkdGg9IjIwMCIgaGVpZ2h0PSIyMDAiIHZpZXdCb3g9IjQwIC01OS44IDIwMCAyMDAiIGVuYWJsZS1iYWNrZ3JvdW5kPSJuZXcgNDAgLTU5LjggMjAwIDIwMCIgeG1sOnNwYWNlPSJwcmVzZXJ2ZSI+PHBhdGggZmlsbD0iIzNENUE5QSIgZD0iTTE0MC00OS44YzQ5LjYgMCA5MCA0MC40IDkwIDkwcy00MC40IDkwLTkwIDkwYy00OS42IDAtOTAtNDAuNC05MC05MFM5MC40LTQ5LjggMTQwLTQ5LjhNMTQwLTU5LjhDODQuOC01OS44IDQwLTE1IDQwIDQwLjNzNDQuOCAxMDAgMTAwIDEwMGM1NS4yIDAgMTAwLTQ0LjggMTAwLTEwMFMxOTUuMy01OS44IDE0MC01OS44TDE0MC01OS44eiIvPjxwYXRoIGZpbGw9IiMzQzVBOTkiIHN0cm9rZT0iIzQ4NUM5MCIgZD0iTTE0Mi44LTI0LjdMMTQzLTUuNWMtNC4yIDkuOC0xMi42IDI0LjgtMTguNiAzMC41bC0wLjgtMSAtNDEuNS0wLjFjLTIuMS0wLjEtNC40IDIuNS00LjMgNC4zbDQgNTcuM2MwLjEgMS45IDIgNC4yIDMuOSA0LjJoMzdjMS40IDAgMy40LTEuNSAzLjUtMy40bDAuMi01YzEuMiAxLjggNC40IDQuNSA3LjMgNC41aDQ5LjljNi4zIDAgMTUuOS03LjIgMTIuNy0xOS4zIDMuNS0zLjEgNS43LTguMyAzLjUtMTQuOSAzLjctMy4zIDYuNS05LjUgMy4zLTE1IDEwLTkgMy41LTIxLjYtNC4yLTIxLjZoLTMzLjZjMS4yLTYgMy4yLTEyIDMuMS0xOS45IC0wLjEtNi45LTQuMy0xNi45LTcuNC0yMi4yQzE1OS45LTI5LjUgMTQ4LjItMzMuNyAxNDIuOC0yNC43TDE0Mi44LTI0Ljd6Ii8+PHBhdGggZmlsbD0iIzZFN0ZCMyIgZD0iTTgyLjkgMjkuNGgzNC43bDAuMSA1NS40SDg2LjlMODIuOSAyOS40eiIvPjxwYXRoIGZpbGw9IiNGRkZGRkYiIGQ9Ik0xNTkuNyAyMC4yYzEuNi03IDIuNy0xNC42IDMuNS0yMi4xIDAuMi0xLjggMC4yLTMuNC0wLjEtNC45IC0xLTYtMy4zLTExLjMtNS42LTE2LjcgLTIuMS0xLjgtNi40LTIuNS05LjUgMC4ydjE4LjhjLTUuMyAxMS4yLTExLjMgMjItMTguNyAzMS45bC03IDUuMnY0My42aDQuOGMwIDAgNi40IDQuMiA2LjggNCAwLjYgMC4zIDQ5LjkgMC4xIDQ5LjkgMC4xIDIuNiAwLjEgMTIuMy03LjYgNi4yLTEzLjQgLTAuOC0wLjUtMC43LTIuMSAwLjEtMi43IDMuOC0wLjcgOC41LTguNSAzLjYtMTIuNiAtMC44LTEtMC4yLTIuNSAwLjctMi42IDIuOC0xLjUgNy44LTYuNSAzLjMtMTIuNSAtMC45LTAuNS0wLjktMS44LTAuMS0yLjUgNC4zLTAuNiA5LjgtMTEuMiAwLjctMTMuOEwxNTkuNyAyMC4yeiIvPjwvc3ZnPg==" title="Like Spark on Facebook" alt="Like us on facebook"></a>
        <div id="github-star"><img id="star-bg" src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAFwAAAAUCAIAAACPhs0OAAAACXBIWXMAAAsTAAALEwEAmpwYAAAKT2lDQ1BQaG90b3Nob3AgSUNDIHByb2ZpbGUAAHjanVNnVFPpFj333vRCS4iAlEtvUhUIIFJCi4AUkSYqIQkQSoghodkVUcERRUUEG8igiAOOjoCMFVEsDIoK2AfkIaKOg6OIisr74Xuja9a89+bN/rXXPues852zzwfACAyWSDNRNYAMqUIeEeCDx8TG4eQuQIEKJHAAEAizZCFz/SMBAPh+PDwrIsAHvgABeNMLCADATZvAMByH/w/qQplcAYCEAcB0kThLCIAUAEB6jkKmAEBGAYCdmCZTAKAEAGDLY2LjAFAtAGAnf+bTAICd+Jl7AQBblCEVAaCRACATZYhEAGg7AKzPVopFAFgwABRmS8Q5ANgtADBJV2ZIALC3AMDOEAuyAAgMADBRiIUpAAR7AGDIIyN4AISZABRG8lc88SuuEOcqAAB4mbI8uSQ5RYFbCC1xB1dXLh4ozkkXKxQ2YQJhmkAuwnmZGTKBNA/g88wAAKCRFRHgg/P9eM4Ors7ONo62Dl8t6r8G/yJiYuP+5c+rcEAAAOF0ftH+LC+zGoA7BoBt/qIl7gRoXgugdfeLZrIPQLUAoOnaV/Nw+H48PEWhkLnZ2eXk5NhKxEJbYcpXff5nwl/AV/1s+X48/Pf14L7iJIEyXYFHBPjgwsz0TKUcz5IJhGLc5o9H/LcL//wd0yLESWK5WCoU41EScY5EmozzMqUiiUKSKcUl0v9k4t8s+wM+3zUAsGo+AXuRLahdYwP2SycQWHTA4vcAAPK7b8HUKAgDgGiD4c93/+8//UegJQCAZkmScQAAXkQkLlTKsz/HCAAARKCBKrBBG/TBGCzABhzBBdzBC/xgNoRCJMTCQhBCCmSAHHJgKayCQiiGzbAdKmAv1EAdNMBRaIaTcA4uwlW4Dj1wD/phCJ7BKLyBCQRByAgTYSHaiAFiilgjjggXmYX4IcFIBBKLJCDJiBRRIkuRNUgxUopUIFVIHfI9cgI5h1xGupE7yAAygvyGvEcxlIGyUT3UDLVDuag3GoRGogvQZHQxmo8WoJvQcrQaPYw2oefQq2gP2o8+Q8cwwOgYBzPEbDAuxsNCsTgsCZNjy7EirAyrxhqwVqwDu4n1Y8+xdwQSgUXACTYEd0IgYR5BSFhMWE7YSKggHCQ0EdoJNwkDhFHCJyKTqEu0JroR+cQYYjIxh1hILCPWEo8TLxB7iEPENyQSiUMyJ7mQAkmxpFTSEtJG0m5SI+ksqZs0SBojk8naZGuyBzmULCAryIXkneTD5DPkG+Qh8lsKnWJAcaT4U+IoUspqShnlEOU05QZlmDJBVaOaUt2ooVQRNY9aQq2htlKvUYeoEzR1mjnNgxZJS6WtopXTGmgXaPdpr+h0uhHdlR5Ol9BX0svpR+iX6AP0dwwNhhWDx4hnKBmbGAcYZxl3GK+YTKYZ04sZx1QwNzHrmOeZD5lvVVgqtip8FZHKCpVKlSaVGyovVKmqpqreqgtV81XLVI+pXlN9rkZVM1PjqQnUlqtVqp1Q61MbU2epO6iHqmeob1Q/pH5Z/YkGWcNMw09DpFGgsV/jvMYgC2MZs3gsIWsNq4Z1gTXEJrHN2Xx2KruY/R27iz2qqaE5QzNKM1ezUvOUZj8H45hx+Jx0TgnnKKeX836K3hTvKeIpG6Y0TLkxZVxrqpaXllirSKtRq0frvTau7aedpr1Fu1n7gQ5Bx0onXCdHZ4/OBZ3nU9lT3acKpxZNPTr1ri6qa6UbobtEd79up+6Ynr5egJ5Mb6feeb3n+hx9L/1U/W36p/VHDFgGswwkBtsMzhg8xTVxbzwdL8fb8VFDXcNAQ6VhlWGX4YSRudE8o9VGjUYPjGnGXOMk423GbcajJgYmISZLTepN7ppSTbmmKaY7TDtMx83MzaLN1pk1mz0x1zLnm+eb15vft2BaeFostqi2uGVJsuRaplnutrxuhVo5WaVYVVpds0atna0l1rutu6cRp7lOk06rntZnw7Dxtsm2qbcZsOXYBtuutm22fWFnYhdnt8Wuw+6TvZN9un2N/T0HDYfZDqsdWh1+c7RyFDpWOt6azpzuP33F9JbpL2dYzxDP2DPjthPLKcRpnVOb00dnF2e5c4PziIuJS4LLLpc+Lpsbxt3IveRKdPVxXeF60vWdm7Obwu2o26/uNu5p7ofcn8w0nymeWTNz0MPIQ+BR5dE/C5+VMGvfrH5PQ0+BZ7XnIy9jL5FXrdewt6V3qvdh7xc+9j5yn+M+4zw33jLeWV/MN8C3yLfLT8Nvnl+F30N/I/9k/3r/0QCngCUBZwOJgUGBWwL7+Hp8Ib+OPzrbZfay2e1BjKC5QRVBj4KtguXBrSFoyOyQrSH355jOkc5pDoVQfujW0Adh5mGLw34MJ4WHhVeGP45wiFga0TGXNXfR3ENz30T6RJZE3ptnMU85ry1KNSo+qi5qPNo3ujS6P8YuZlnM1VidWElsSxw5LiquNm5svt/87fOH4p3iC+N7F5gvyF1weaHOwvSFpxapLhIsOpZATIhOOJTwQRAqqBaMJfITdyWOCnnCHcJnIi/RNtGI2ENcKh5O8kgqTXqS7JG8NXkkxTOlLOW5hCepkLxMDUzdmzqeFpp2IG0yPTq9MYOSkZBxQqohTZO2Z+pn5mZ2y6xlhbL+xW6Lty8elQfJa7OQrAVZLQq2QqboVFoo1yoHsmdlV2a/zYnKOZarnivN7cyzytuQN5zvn//tEsIS4ZK2pYZLVy0dWOa9rGo5sjxxedsK4xUFK4ZWBqw8uIq2Km3VT6vtV5eufr0mek1rgV7ByoLBtQFr6wtVCuWFfevc1+1dT1gvWd+1YfqGnRs+FYmKrhTbF5cVf9go3HjlG4dvyr+Z3JS0qavEuWTPZtJm6ebeLZ5bDpaql+aXDm4N2dq0Dd9WtO319kXbL5fNKNu7g7ZDuaO/PLi8ZafJzs07P1SkVPRU+lQ27tLdtWHX+G7R7ht7vPY07NXbW7z3/T7JvttVAVVN1WbVZftJ+7P3P66Jqun4lvttXa1ObXHtxwPSA/0HIw6217nU1R3SPVRSj9Yr60cOxx++/p3vdy0NNg1VjZzG4iNwRHnk6fcJ3/ceDTradox7rOEH0x92HWcdL2pCmvKaRptTmvtbYlu6T8w+0dbq3nr8R9sfD5w0PFl5SvNUyWna6YLTk2fyz4ydlZ19fi753GDborZ752PO32oPb++6EHTh0kX/i+c7vDvOXPK4dPKy2+UTV7hXmq86X23qdOo8/pPTT8e7nLuarrlca7nuer21e2b36RueN87d9L158Rb/1tWeOT3dvfN6b/fF9/XfFt1+cif9zsu72Xcn7q28T7xf9EDtQdlD3YfVP1v+3Njv3H9qwHeg89HcR/cGhYPP/pH1jw9DBY+Zj8uGDYbrnjg+OTniP3L96fynQ89kzyaeF/6i/suuFxYvfvjV69fO0ZjRoZfyl5O/bXyl/erA6xmv28bCxh6+yXgzMV70VvvtwXfcdx3vo98PT+R8IH8o/2j5sfVT0Kf7kxmTk/8EA5jz/GMzLdsAAAAgY0hSTQAAeiUAAICDAAD5/wAAgOkAAHUwAADqYAAAOpgAABdvkl/FRgAABRhJREFUeNrcWG1sU2UUPi33A2071w9pO5ao6VrjjFs7E8zWOiaJUCYuSCALqES2ZPOHBDuCbAEBQwwzshvWaKJ8NfgDBeofA5lfJKZFoskoBrcZ1yXiiO02dtfl3rcf97a7rz/uspS7+sPIdonnx7nJOff2nDznOafveTUYY5ZlU6mUKIqgtlAUZTQazWazwi5nKAjCkkanaVqOTrAsm81mKyoqaJrGGGs0GhV1Pp+fmprCGFsslmJEEEJ2u52iqCUFRRTF6elpANCmUimj0UhRlCRJAKCuJgjCYrGkUqniXGdmZsxm81IjIvPUbDbPzMwQoihSFIUxBgCF5nk+FovF43EAcDqddXV1BoOh5Jv3UVMUlc/nFQWkaXp5+pemaVEUiYVsFHL69OmLFy8ihBYser2+tbW1vb0d/u+ilR+Kiu3fv//s2bPFiAAAQujMmTPd3d1LypSSFVIBFEVvnzhxIhqNbtu2bdeuXTabze12ezweq9Xa1ta2cePGSCRy6tSpUhNB+P2LwMsveBsa1nd8GPlLkgDg7o3P+r9n/+1kUV0IBVOSyeSlS5cAwOPxNDY2KpolEokMDAyEQqHm5ma73X5PhRMDx4M/NwR/PFJx+Y2tPZ8+N3Dk+fHzuz8Zf3fTf2HK7OxsWVmZCkzBRRKJRGRHVVUVXiRWq3UBHaVvdjIOAJKQW/1KaHDw2LrCla7OCwA/Hd3k6x/CODd6oatlnc/n8+84/F0CYzzU7/P5urq7/b4dF27f80vFiPA8b7PZVJ4pyWQSANxut5yKooYul8vhcAAAz/NK7zNbeuqpy2+3vMVcuTWZyYnmzR+HtgPUH/56cJ8bj4YPhJ/quz44GO5YefW98AhgYgUAzK7Z88Pgl687SjBFLURKzBQ5J3nElux52YUQUnrzq5qD34SDHY/+cqz9pdf6f0VCdg4DgJRPZwWobgsdd/72wTs7Oz4aBdBq52dH3dOVmUxOKDxYM0XJFLvdDgBjY2OJRGIxU+Lx+OTkJACU4JGUz2XJijWt758PH352PHzu+vR8zTEAcFd7Wl79/E/fm30ne+rnIwIAgCRiLElSCaaUl5cbDIaJiQl1QCnuZ6/XCwBWq/XgwYOxWKzYFYvFent75c+8Xq9ipLAD+9Y2dn11O5tJJ/9IALV61SMYAEAAEWN8NzEiwhMN7koYv3WjCJN7gytnilq4lGDKhg0bnE5nTU1NIBAIBoOyvbe3NxAIjI2NAYDf71/MlFWbDxzfkjm5s6mpafflx7tCAQ+JH6vf7ho6unX9yZEnX9y7yfXtnrXr90bpJjvciI9jwAAwr//536e8vJzjuGUGRTM8PFxZWVm8lSGEAoGA3++vqalxuVyyPRqNHjp0CAAcDgfDMGVlZcqNTks99DBNaDUAAHhOzKQFiVypW0lqNTAnorxWdmJJwlqtpiAgTBjIFXN5Pp1VboZ37typrq5eSHF4eNjlci0bIqOjoyVOtHq9nmGYa9eudXZ2xmIx2a7T6QCgtraWYZjSG5AkZhDPycKnc3OAcT4rW9K5BSePEOI4PiPgbJrjuHT2QTzREvLMV9RKp9MxDHPz5k2bzSZ7rVZrX19fbW2tRqNZ/P791eqDQpJkoVAgCGJxxdxu9wKPbDZbyZPLfdeiKJIkqdjoBUFYnkVZEASKogiTycRxnF6vl3FRVwqFAkLIaDQWG00mE8uyFotlGS6ZWJY1mUyaB+o6kiRJk8mk1nUkRVFy9L8HANLhIkH01sUwAAAAAElFTkSuQmCC"><iframe id="star-frame" src="" frameborder="0" scrolling="0" width="95px" height="20px"></iframe></div>
    </div>
</div>
<div class="container">
    <div class="stack-container">
        <div class="left-panel cf <#if !frames?has_content>empty</#if>">
            <header>
                <div class="exception">
                    <div class="exc-title">
                        <#list name as namePart><span>${namePart}</span></#list>
                    </div>
                    <p class="exc-message">
                        <#if message == "">No Exception Message<#else>${message}</#if>
                    </p>
                </div>
            </header>
            <div id="exc-btns">
                <button id="copy-button" class="clipboard" data-clipboard-text="${plain_exception}" title="Copy exception details to clipboard">
                    Copy stacktrace
                </button>
                <button id="google-button" data-google-query="<#list name as namePart>${namePart}<#if !namePart?is_last>+</#if></#list>" title="Google this exception">
                    Google exception
                </button>
            </div>
            <div class="frames-description">Stack frames (${frames?size}):</div>
            <div class="frames-container">
                <#assign found = false>
                <#list frames as frame>
                    <div class="frame<#if frame.code??> has-code<#if !found> active<#assign found = true></#if></#if>" id="frame-line-${frame?index}">
                        <div class="frame-method-info">
                            <span class="frame-index">${frames?size - frame?counter}</span>
                            <span class="frame-class">${frame.class}</span>
                            <span class="frame-function">${frame.function}</span>
                        </div>
                        <span class="frame-file">${frame.file}<#if frame.line != "-1"><span class="frame-line">${frame.line}</span></#if></span>
                    </div>
                </#list>
            </div>
        </div>
        <div class="details-container cf">
            <div class="frame-code-container <#if !frames?has_content>empty</#if>">
                <#assign found = false>
                <#list frames as frame>
                    <div class="frame-code<#if frame.code??> has-code<#if !found> active<#assign found = true></#if></#if>" id="frame-code-${frame?index}">
                        <div class="frame-file">
                            <strong>${frame.file}</strong>
                            <#if frame.canonical_path??>
                                (${frame.canonical_path})
                            </#if>
                        </div>
                        <#if frame.code??>
                            <pre class="code-block prettyprint linenums:${frame.code_start}"><code class="language-java">${frame.code}</code></pre>
                        </#if>
                        <div class="frame-comments <#if !frame.comments?has_content>empty</#if>">
                        <#--<#list 0..frames[i].comments?size-1 as commentNo>
                          <#assign comment = frames[i].comments[commentNo]>
                          <div class="frame-comment" id="comment-${i}-${commentNo}">
                            <span class="frame-comment-context">${comment.context}</span>
                            ${comment.text}
                          </div>
                        </#list>-->
                        </div>
                    </div>
                </#list>
            </div>
            <div class="details">
                <div class="data-table-container" id="data-tables">
                    <#list tables?keys as label>
                        <#assign data = tables[label]>
                        <div class="data-table">
                            <h3>${label}</h3>
                            <#if data?has_content>
                                <table class="data-table">
                                    <thead>
                                    <tr>
                                        <td class="data-table-k">Key</td>
                                        <td class="data-table-v">Value</td>
                                    </tr>
                                    </thead>
                                    <#list data?keys as k>
                                        <tr>
                                            <td><div>${k}</div></td>
                                            <td><div>${data[k]}</div></td>
                                        </tr>
                                    </#list>
                                </table>
                            <#else>
                                <span class="empty">EMPTY</span>
                            </#if>
                        </div>
                    </#list>
                </div>
                <!--<div class="data-table-container" id="handlers">
                  <label>Registered Handlers</label>
                    <div class="handler active">
                      1. SomeHandler
                    </div>
                </div>-->
            </div>
        </div>
    </div>
</div>
<div id="spark-footer">
    The Spark Debug Screen is a port of
    <a href="https://github.com/filp/whoops" target="_blank">Whoops</a>.
    It was ported by
    <a href="https://github.com/mschurr" target="_blank">Matthew Schurr</a>.
</div>
<script><#include "/public/sparkdebugtools/js/lib/sparkdebugtools_zepto.min.js"></script>
<script><#include "/public/sparkdebugtools/js/lib/sparkdebugtools_prettify.min.js"></script>
<script><#include "/public/sparkdebugtools/js/lib/sparkdebugtools_clipboard.min.js"></script>
<script><#include "/public/sparkdebugtools/js/sparkdebugtools_shrink-stackframes.js"></script>
<script><#include "/public/sparkdebugtools/js/sparkdebugtools_main.js"></script>
<noscript><style>.frames-container{display:block}</style></noscript
</body>
</html>
</#escape>
