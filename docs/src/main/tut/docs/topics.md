---
layout: docs
title: "Configuration Topics"
position: 2
permalink: /docs/topics
---

# Configuration Topics
The following sections describe common topics when working with Ciris and configurations as code.

- [Encoding Validation](/docs/validation) describes how to use [refinement types](/docs/refined-module) to encode validation in the types of your configuration, for increased safety and confidence in your configurations. Validating your configuration values is a critical factor in preventing latent errors, as being able to detect configuration errors early on can reduce failure damage[^1].
- [Multiple Environments](/docs/environments) explains how to use [enumeratum](/docs/enumeratum-module) enumerations to model and work with multiple environments in your configurations. This includes being able to have different configuration values for different environments, and being able to change the configuration loading process depending on the environment.
- [Logging Configurations](/docs/logging) shows how you can log your configurations for debug purposes, while not exposing any secret configuration values. This is done with a [`Secret`][Secret] wrapper type, and either just `print`, or with the [`Show`][Show] type class from [cats](/docs/cats-module), together with type class instance derivation from [kittens][kittens].

---

[^1]: For example, refer to the paper [Early Detection of Configuration Errors to Reduce Failure Damage](https://www.usenix.org/system/files/conference/osdi16/osdi16-xu.pdf) and Leif Wickland's presentation [Defusing the Configuration Time Bomb](http://leifwickland.github.io/presentations/configBomb/) on the subject of configuration errors and validation.

[kittens]: https://github.com/milessabin/kittens
[Secret]: /api/ciris/Secret.html
[Show]: https://typelevel.org/cats/typeclasses/show.html
