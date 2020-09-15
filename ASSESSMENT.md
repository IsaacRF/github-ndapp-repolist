# XING Android Coding Challenge

Welcome to the XING Coding Challenge! This challenge will give us an idea about your coding skills.


## Prerequisites

* Feel free to use Java or Kotlin for this challenge.
* Do not focus too much on the design of the UI.
* The usage of third party libraries is explicitly allowed.
* Provide a comprehensive git history.
* If you want to use different branches, please make sure that they’ll be merged into master branch when you’ll finish the task.
* If your API request limit exceeds, you can generate and use a personal access token [here](https://github.com/settings/applications) and add `?access_token=<YOUR_ACCESS_TOKEN>` to the request URLs.


## Goals

1. Clone this repository. Use it as your working directory.
2. Bootstrap a new empty application.
3. Request the GitHub API to show [XING's public repositories](https://api.github.com/orgs/xing/repos) and parse the JSON response. You can find documentation to the call [here](https://developer.github.com/v3/repos/#list-organization-repositories).
4. Display a list of repositories, each entry showing
	* name of repository
	* description
	* login of the owner
5. Show a light green background if the repository is forked and a white one otherwise.
6. The business logic should be tested by unit tests.



## Bonus Goals

* Cache the data so it is available offline.
* Implement a load more mechanism. The load more should be triggered when the scrolling is close to reaching the end of the list. Check out the [pagination documentation](https://developer.github.com/v3/#pagination) for more info.
* On a long-press on a list item show a dialog to ask if should go to repository html_url or owner html_url which is then opened in the browser.
* Display owner’s avatar image and cache it accordingly.

## General Advice and Tips
* Please keep code as simple as possible and remove any unused code.
* Project is well-structured.
* We would prefer if you follow the material design guidelines.
* Keep in mind the [Separation of concerns](https://en.wikipedia.org/wiki/Separation_of_concerns), and the [SOLID principles](https://en.wikipedia.org/wiki/SOLID_(object-oriented_design)).
* You can use a reactive approach (Ex. RxJava).
* Make sure the app runs on a ICS+ device.
* Don't forget the tests, they are really important for us.
* Be conscious and consistent regarding your coding style.
* Have fun reading and writing code.
* If you have any final comments about your result please let us know via [final_notes.md](final_notes.md)
* Once you finish create a release and notify barcelona-hiring@xing.com

Now, let's get started. We wish you good luck!
