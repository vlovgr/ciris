# Contributing Guide
This guide is for people who would like to be involved in building Ciris.  
As a free and open source project, we gladly welcome contributions.

## Find something to work on
Looking for a way to help out? Take a look at the [open issues](https://github.com/vlovgr/ciris/issues). Choose an issue that looks interesting to you. If you are new to the project, the label [`low-hanging fruit`](https://github.com/vlovgr/ciris/labels/low-hanging%20fruit) can help you identify suitable issues to work on. Before you start working on an issue, make sure it's not already assigned to someone and that nobody else has left a comment saying they're working on it. Of course, you can also comment on an issue someone else is working on and offer to collaborate. If you're unsure where to start, you can always ask for help in the [Gitter](https://gitter.im/vlovgr/ciris) chat room.

Have an idea for something new? That's great! We recommend you make sure it belongs in Ciris before you put effort into creating a pull request. The preferred ways to do that are to either:

* [create an issue](https://github.com/vlovgr/ciris/issues/new) describing your idea, or
* ask for feedback in the [Gitter](https://gitter.im/vlovgr/ciris) chat room.

## Let us know you are working on it
If there is already an open issue for the task you're working on, leave a comment to let people know you're working on it. If there isn't already an issue and it's a non-trivial task, it's a good idea to create a new issue and put a note that you're working on it. This prevents contributors from duplicating effort.

## Build the project
First you'll need to checkout a local copy of the code base.
```
git clone https://github.com/vlovgr/ciris.git
```

To build the project, you'll need to have [SBT](https://www.scala-sbt.org) installed.

Run `sbt`, and then `validate` to ensure everything is setup correctly.  
Below is a list of some useful sbt commands to help you get started.

* `clean`: cleans any compiled output; to start clean again.
* `compile`: compiles the application source files.
* `docs/makeMicrosite`: generates the website (requires [Jekyll](https://jekyllrb.com)).
* `docs/tut`: compiles the documentation in [`docs`](https://github.com/vlovgr/ciris/tree/master/docs).
* `docs/unidoc`: generates the [Scaladoc](https://docs.scala-lang.org/style/scaladoc.html) API documentation.
* `docTests`: run tests to check API documentation code examples.
* `generateReadme`: generates the readme from the [index page](https://github.com/vlovgr/ciris/blob/master/docs/src/main/tut/index.md).
* `generateScripts`: generates the script files in the [`scripts`](https://github.com/vlovgr/ciris/tree/master/scripts) directory.
* `test`: run tests for the application sources.
* `validate`: compile and run the tests with code coverage enabled.
* `validateDocs`: run `docTests`, `docs/unidoc`, and `docs/tut` as described above.
* `validateNative`: run tests for Scala Native modules (requires [Scala Native](http://www.scala-native.org) setup).

## Submit a pull request
Before you open a pull request, you should make sure that `sbt +validate` runs successfully. [Travis CI](https://travis-ci.org/vlovgr/ciris) will run this as well, but it may save you some time to be alerted to problems earlier on. Once Travis CI has run, [Codecov](https://codecov.io/gh/vlovgr/ciris) will check the code coverage and comment on your pull request with the results.

If your pull request addresses an existing issue, please include the issue number in the pull request message, or as part of your commit message. For example, if your pull request addresses issue number 52, then please include `fixes #52` in your pull request message, or in the commit message.

## Grant of license
Ciris is licensed under the [MIT License](https://opensource.org/licenses/MIT). Opening a pull request signifies your consent to license your contributions under this license.

## Code of conduct
Ciris supports the [Typelevel Code of Conduct](https://typelevel.org/conduct.html). To provide a safe and friendly environment for teaching, learning, and contributing, it is expected that participants follow the code of conduct in all official channels, including on GitHub and in the Gitter chat room.

## Credits
This document is heavily based on the [http4s contributing guide](http://http4s.org/contributing).
