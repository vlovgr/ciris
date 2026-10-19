# Security Policy

## Reporting a Security Issue

To report a security issue, please use one of the following methods.

1. Navigate to the "Security and quality" tab at the top of this repository, click the "Report a vulnerability" button, and complete the form as much as possible.
2. Email the [Security Team](#security-team) with a description of the issue, the steps you took to create the issue, affected versions, and, if known, mitigations for the issue.

The [Security Team](#security-team) will attempt to respond within 5 working days of your report. If the issue is confirmed as a vulnerability, we will open a Security Advisory. This project follows a 90-day disclosure timeline.

## Procedure

1. A GitHub Security Advisory will be created in this repository.
2. A project member works privately with the reporter to resolve the vulnerability.
3. The project creates a new release to deliver the fix.
4. The project publicly announces the vulnerability and describes how to apply the fix.

## Scala Steward

Security fixes are only released for the latest available version. Users are strongly recommended to use [Scala Steward](https://github.com/scala-steward-org/scala-steward) or similar to automatically receive updates.

## Security Team

| Name                                         | Email              | PGP Public Key                                                                                   |
| -------------------------------------------- | ------------------ | ------------------------------------------------------------------------------------------------ |
| [Viktor Rudebeck](https://github.com/vlovgr) | viktor@rudebeck.nu | [0xE9911A26DD646756](https://keys.openpgp.org/search?q=BAA2CEC4A634FE2D4531DB81E9911A26DD646756) |

## Attribution

This policy was adapted from the [Typelevel Security Policy](https://typelevel.org/security.html), licensed under [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/).
