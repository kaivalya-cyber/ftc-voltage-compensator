# Changelog

All notable changes to this project are documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project roughly adheres to [Semantic Versioning](https://semver.org/).
We use 5xx version numbers (v500.x) by convention so the project's revisions
stay one major-version ahead of any specific FTC SDK binding.

## [v500.2.0] - 2026-06-23

### Added
- `CHANGELOG.md` (this file) \u2014 release history in Keep-a-Changelog v1.1.0
  style with Security / Changed / Added / Removed sections.
- `CONTRIBUTING.md` \u2014 FTC-team guide covering clone-and-test, stub
  interface conventions (mirror only what production sources call),
  coding style (Java 11 source \u2192 Java 8 SDK runtime), PR process
  including fork-then-push flow, and explicit "no test framework / no
  external dependencies" rules.

### Changed
- `.github/workflows/build.yml` cache path now includes
  `~/.gradle/wrapper/dists` so cold-runner wrapper extraction (~30-60 s
  on a fully virgin runner) is also cached.  Key composition already
  includes `gradle.properties` so config-cache toggles invalidate.

## [v500.1.0] - 2026-06-23

### Security
- Pinned `distributionSha256Sum` for `gradle-9.6.0-bin.zip` directly into
  `gradle/wrapper/gradle-wrapper.properties`.  The wrapper now refuses to
  download any tampering of the upstream distribution.
- Added `validateDistributionUrl=true` so the pinned SHA is actually
  verified on every fresh wrapper download (without this flag the SHA
  line is parsed but ignored).

### Changed
- New `actions/cache@v4` step before `Set up JDK 17` persists
  `.gradle/` (configuration cache) and `~/.gradle/caches` (downloaded
  artifacts) across CI runs.  Cache key includes `gradle.properties` so
  toggling `org.gradle.configuration-cache` invalidates the cache.

## [v500.0.0] - 2026-06-22

### Added
- `VoltageCompensator.java` \u2014 adaptive voltage compensator with rolling-
  average smoothing, linear-regression trend prediction, brownout
  protection, and cubic drive-shaping.
- `VoltageCompensatedTeleOp.java` \u2014 reference `LinearOpMode` wiring the
  compensator into a 4-motor mecanum drivetrain, arm motor, position
  servo, and continuous-rotation servo.
- MIT license (`LICENSE`).
- 40 unit tests under `test/` covering initial state, rolling-buffer
  behaviour, compensation cap/floor, brownout + power halving, cubic
  drive curve, narrower servo cap, null-sensor fallback, telemetry
  messages, long-run stability, and linear-regression trend detection.
- Lightweight `./runtests.sh` shell driver so the suite runs with
  nothing more than a JDK \u2014 no Gradle, no FTC SDK download required.
- Stub interfaces under `test/` (VoltageSensor, Range, Telemetry,
  OpMode, LinearOpMode, TeleOp, DcMotor, Servo, HardwareMap, Gamepad)
  so production sources compile against the same test classpath.
- GitHub Actions CI (JDK 17) on push and pull request.
- Optional `build.gradle` + `settings.gradle` for teams that prefer
  Gradle.  `./gradlew runUnitTests` runs the same suite.
- README with usage examples, tunable constants, tests section, build
  options, and FTC Android Studio wiring instructions.

<!-- EVERY MAINTAINER: when tagging a new release, advance the [Unreleased]
     comparison base below to the new tag (e.g. v500.3.0) and add a link
     reference for it. -->
[Unreleased]: https://github.com/kaivalya-cyber/ftc-voltage-compensator/compare/v500.2.0...HEAD
[v500.2.0]: https://github.com/kaivalya-cyber/ftc-voltage-compensator/compare/v500.1.0...v500.2.0
[v500.1.0]: https://github.com/kaivalya-cyber/ftc-voltage-compensator/compare/v500.0.0...v500.1.0
[v500.0.0]: https://github.com/kaivalya-cyber/ftc-voltage-compensator/releases/tag/v500.0.0
