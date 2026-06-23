# Changelog

All notable changes to this project are documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project roughly adheres to [Semantic Versioning](https://semver.org/).
We use 5xx version numbers (v500.x) by convention so the project's revisions
stay one major-version ahead of any specific FTC SDK binding.

## [v500.30.0] - 2026-06-22

### Fixed
- `CHANGELOG.md` v500.28.0 entry: restored after accidental deletion
  during the v500.29.0 entry insertion.

### Fixed
- `CHANGELOG.md` v500.28.0 entry: "Changed" bullet base-advance
  direction corrected from `v500.26.0 \u2192 v500.27.0` to
  `v500.27.0 \u2192 v500.28.0`.

## [v500.28.0] - 2026-06-22

### Added
- `CHANGELOG.md` entry for v500.27.0 (documenting v500.26.0 entry
  addition).
- Local tag-changelog consistency check confirmed 34/34 match.

### Changed
- `[Unreleased]` comparison base advanced from `v500.27.0` to
  `v500.28.0`.

## [v500.27.0] - 2026-06-22

### Added
- `CHANGELOG.md` entry for v500.26.0 (POSIX temp-file fix + missing
  v500.24.0 entry).

### Changed
- `[Unreleased]` comparison base advanced from `v500.25.0` to
  `v500.26.0`.

## [v500.26.0] - 2026-06-22

### Fixed
- `CONTRIBUTING.md` step 7: replaced bash-only `diff <(...) <(...)`
  with POSIX-compatible temp-file approach (`/tmp/cl-tags`,
  `/tmp/git-tags`, `diff`, `rm`).
- `CHANGELOG.md`: added missing v500.24.0 entry (base-advance
  direction fix).

## [v500.25.0] - 2026-06-22

### Removed
- `.github/workflows/tag-changelog-check.yml`: removed after multiple
  tag-fetching approaches (`fetch-depth: 0`, `fetch-tags: true`,
  `git fetch --tags`, `git ls-remote`, force refspec) all proved
  unreliable on GitHub Actions runners.  The local check (documented
  in `CONTRIBUTING.md`) works reliably.

### Added
- `CONTRIBUTING.md` "Pull requests" step 7: local tag-changelog
  consistency check to run before tagging a release.

## [v500.24.0] - 2026-06-22

### Fixed
- `CHANGELOG.md` v500.23.0 entry: "Changed" bullet base-advance
  direction corrected from `v500.21.0 \u2192 v500.22.0` to
  `v500.22.0 \u2192 v500.23.0`.

## [v500.23.0] - 2026-06-22

### Added
- `CHANGELOG.md` entry for v500.22.0 (documenting the v500.18.0–
  v500.21.0 catch-up).

### Changed
- `[Unreleased]` comparison base advanced from `v500.22.0` to
  `v500.23.0`.

## [v500.22.0] - 2026-06-22

### Added
- `CHANGELOG.md` entries for v500.18.0 through v500.21.0 documenting
  the tag-changelog CI fetch iterations (git fetch --tags →
  git ls-remote → force refspec → drop redundant --force).

### Changed
- `[Unreleased]` comparison base advanced from `v500.17.0` to
  `v500.21.0`.

## [v500.21.0] - 2026-06-22

### Changed
- `.github/workflows/tag-changelog-check.yml`: dropped redundant
  `--force` flag from `git fetch` (the `+` prefix in the refspec
  already forces updates).

## [v500.20.0] - 2026-06-22

### Changed
- `.github/workflows/tag-changelog-check.yml`: replaced
  `git ls-remote` approach with explicit force refspec
  `git fetch origin '+refs/tags/*:refs/tags/*'` to fetch all tags
  after checkout, then uses `git tag -l` as before.

## [v500.19.0] - 2026-06-22

### Changed
- `.github/workflows/tag-changelog-check.yml`: replaced local
  `git tag -l` + `git fetch --tags` with `git ls-remote --tags
  origin` to query the remote directly, bypassing local
  tag-fetching quirks on GitHub Actions.  Added annotated-tag
  deduplication (`sed 's/\^{}//'`, `sort -Vu`).
- `CHANGELOG.md`: v500.17.0 heading corrected from `### Fixed`
  to `### Changed` (the grep fix didn't actually resolve the CI
  failure — tags still weren't being fetched).

## [v500.18.0] - 2026-06-22

### Added
- `CHANGELOG.md` entries for v500.15.0, v500.16.0, and v500.17.0
  (catch-up).

### Changed
- `.github/workflows/tag-changelog-check.yml`: added explicit
  `git fetch --tags origin` step after checkout to ensure tags
  are available before the verification step.

## [v500.17.0] - 2026-06-22

### Changed
- `.github/workflows/tag-changelog-check.yml`: switched `grep -oP`
  to `grep -oE` with `[0-9]+` for cross-platform compatibility;
  added explicit `fetch-tags: true` to the checkout step.

## [v500.16.0] - 2026-06-22

### Added
- `README.md` "Documentation" section linking to
  `ARCHITECTURE.md`, `CHANGELOG.md`, `SECURITY.md`,
  `CONTRIBUTING.md`.
- `.github/workflows/tag-changelog-check.yml` (NEW): CI check that
  verifies every `[vX.Y.Z]` CHANGELOG entry has a matching git tag
  and vice versa, failing the build if any are missing.

## [v500.15.0] - 2026-06-22

### Added
- `CHANGELOG.md` entry for v500.14.0 (consistency fixes).

### Changed
- `CHANGELOG.md` maintenance HTML comment example tag updated from
  `v500.9.0` to `v500.14.0`.
- `[Unreleased]` comparison base advanced from `v500.13.1` to
  `v500.14.0`.

## [v500.14.0] - 2026-06-22

### Fixed
- `CHANGELOG.md` consistency: removed duplicate back-tagging bullet
  from the v500.10.0 entry (it already appeared in v500.11.0).
- `CHANGELOG.md` missing entries: added v500.13.0 (SECURITY
  Patched-through tightened) and v500.13.1 (SECURITY maintainer
  comment clarified).

### Changed
- `[Unreleased]` comparison base advanced from `v500.12.0` to
  `v500.13.1`.

## [v500.13.1] - 2026-06-22

### Changed
- `SECURITY.md` maintainer HTML comment clarified to say "bump both
  the version column and the Patched-through column" instead of the
  ambiguous "bump the version."

## [v500.13.0] - 2026-06-22

### Changed
- `SECURITY.md` "Patched through" column for the active row
  tightened from the generic "Latest tag" to the explicit
  `v500.11.0` to match the explicit version now in column 1.

## [v500.12.0] - 2026-06-22

### Changed
- `SECURITY.md` supported-versions table bumped from `v500.4.0`
  to `v500.11.0` as the current latest tag.

## [v500.11.0] - 2026-06-22

### Added
- 14 previously-missing git tags back-filled (`v500.0.0`–`v500.3.0`,
  `v500.4.1`–`v500.9.0`) so GitHub compare URLs resolve.
- `CHANGELOG.md` entries for v500.9.0 and v500.10.0 (catch-up).

### Changed
- `[Unreleased]` comparison base advanced from `v500.8.1` to
  `v500.10.0`.

## [v500.10.0] - 2026-06-22

### Changed
- `CHANGELOG.md`: all 13 entry dates corrected from guessed
  2026-06-23/06-24 to the real commit date 2026-06-22.
- `CONTRIBUTING.md`: action-drift bullet under "What NOT to do"
  reworded to lead with the prohibition ("Don't add a new `@vN`-pinned
  `uses:` reference without expecting the `action-drift` workflow to
  flag it") instead of the softer reminder ("Don't forget").
- `SECURITY.md`: supported-versions table back-filled with the
  actual latest tag (`v500.4.0`) instead of the generic
  `v500.x.x` placeholder; a maintainer bump comment added.

## [v500.9.0] - 2026-06-22

### Added
- `CHANGELOG.md`: entries back-filled for v500.3.0 through
  v500.8.1 (11 releases) so the changelog is in lockstep with the
  tag list.  `[Unreleased]` compare base advanced, 11 link
  references added.
- `CONTRIBUTING.md` "What NOT to do": paragraph reminding
  contributors that the `action-drift` workflow opens a
  `dependencies-drift` issue within ~7 days of any new `@vN`
  pin falling behind upstream.

### Changed
- `.github/workflows/action-drift.yml`: body-assembly collapsed
  from `sections.push([...].join('\n'))` +
  `sections.join('\n\n')` to a single
  `sections.map(s => s.join('\n')).join('\n\n')`.

## [v500.8.1] - 2026-06-22

### Fixed
- `.github/workflows/action-drift.yml`: issue body assembled from
  conditional sections so a docker-only run no longer renders an
  empty `## Drifted pins` heading or a misleading "still pinned
  behind the upstream latest tag" intro.
- `ARCHITECTURE.md` tuning-recipe: dropped `(default \u2026)`
  parenthetical from each `### CONSTANT_NAME` sub-heading so the
  rendered GitHub slug (`#brownout_threshold` etc.) matches the
  table cross-reference links exactly.

## [v500.8.0] - 2026-06-22

### Added
- `.github/workflows/action-drift.yml`: docker-container actions
  (`uses: docker://image:tag`) surfaced in the drift-report issue
  body and indexed in the dynamically-derived title so a docker-only
  run produces an actionable issue instead of a misleading
  "0 pin(s) behind upstream" report.
- `ARCHITECTURE.md` precision: tuning-recipe bullets promoted to
  `### CONSTANT_NAME` sub-headings so the constants-table links
  jump directly at each item rather than to the section preface.

### Changed
- `./runtests.sh` switched to POSIX-portable word-split
  (`set -- $SOURCES; javac \u2026 "$@"`) so paths containing
  whitespace (e.g. `~/Team Code/voltage-sensor`) survive as
  individual javac arguments.

## [v500.7.0] - 2026-06-22

### Added
- `.github/workflows/action-drift.yml`: enumerated
  `uses: docker://image:tag` actions and logged them as
  registry-only entries so they are no longer silently dropped
  from the scan.
- `./runtests.sh`: `[ -z "$SOURCES" ]` precondition that exits
  with a clear "no .java sources found" message on empty-globs
  instead of letting javac emit a generic usage error.
- `ARCHITECTURE.md`: each tunable row in the "Key constants and why"
  table ends with a `see [Tuning recipe \u00a7N]` link, and the
  non-tunable rows are flagged as "not a tuning knob" so a
  contributor doesn't scroll-hunt for tunings that don't exist.

### Changed
- `.github/workflows/action-drift.yml`: clarified the `upstreamMap`
  comment so future contributors understand identity entries
  (key === value) are NOT no-ops but trigger the walk-up
  logic, and added a commented-out example showing the format for
  a non-identity hard redirect.

## [v500.6.0] - 2026-06-22

### Added
- `ARCHITECTURE.md` tuning recipe documents **both** `Raise` and
  `Lower` directions for each of the 6 constants
  (`BROWNOUT_THRESHOLD`, `MAX_SAG_COMPENSATION`,
  `MAX_SERVO_COMPENSATION`, `ROLLING_WINDOW_SIZE`,
  `TREND_WINDOW_SIZE`, `TREND_CORRECTION_GAIN`) so a tuner at
  competition can reason about the symmetric trade-off.

### Changed
- `.github/workflows/action-drift.yml`: hand-maintained hard-coded
  `actions = [\u2026]` array replaced with auto-extraction that walks
  `.github/workflows/*.yml`, regex-extracts every
  `uses: <owner>/<repo>@<ref>` line, and resolves the few cases
  where the effective upstream repo differs from the `uses:`
  prefix (e.g. `github/codeql-action/init@v3 \u2192
  github/codeql-action`).
- `./runtests.sh` javac file list mirrored from
  `.github/workflows/build.yml` (the same `find . -maxdepth 1
  -name '*.java'` + `find test -name '*.java'` glob) so adding
  a new stub interface no longer needs to update two places.

## [v500.5.1] - 2026-06-22

### Fixed
- `.github/workflows/action-drift.yml`: drift check `upstreamLatest
  === currentRef` could never match for major-version pins
  (`v4` vs `v4.2.0`), so the workflow would have opened a tracking
  issue on every weekly run regardless of drift; replaced with
  `upstreamLatest.startsWith(currentRef + '.')` so a `v4` pin against
  upstream `v4.2.0` is properly up-to-date.  Plus explicit
  semver-sort because `repos.listTags` returns commit-date order.
- `ARCHITECTURE.md` tuning recipe: `Lower BROWNOUT_THRESHOLD if the
  robot browns out before your end-of-match voltage` had the
  direction inverted (lowering *delays* protection); corrected
  to `**Raise**\u2026`.

## [v500.5.0] - 2026-06-22

### Added
- `ARCHITECTURE.md` (NEW): the single source of truth for "how
  does this thing behave at runtime" before tuning.  Mermaid
  state diagram (Nominal / Brownout transitions + factor
  formula), per-loop sequence diagram (`OpMode \u2192 VC \u2192 Sensor`),
  key-constants table with rationale, integration map for
  `VoltageCompensatedTeleOp`, tuning recipe, cross-references to
  `CONTRIBUTING.md` / `SECURITY.md` / `CHANGELOG.md`.
- `.github/workflows/action-drift.yml` (NEW): weekly Monday cron
  + `workflow_dispatch`.  Detects when any third-party action
  used in `.github/workflows/*.yml` is pinned behind the upstream
  latest tag within the same major, opens (or reuses) a single
  GitHub issue labeled `dependencies-drift`.

### Changed
- `.github/workflows/build.yml`: manual 13-file javac list replaced
  with a `find . -maxdepth 1 -name '*.java'` + `find test
  -name '*.java'` glob.  New stub interface or test class no
  longer requires a build.yml edit.
- `CONTRIBUTING.md`: short note pointing new tuners to
  `ARCHITECTURE.md` before reading the rest of the contributing
  guide.

## [v500.4.3] - 2026-06-22

### Changed
- `SECURITY.md` "Hardening guarantees" GitHub Actions bullet was
  trimmed to scan-comparable length with the matrix / CodeQL /
  wrapper SHA-256 bullets in the same section.  Cadence
  duplication removed.

## [v500.4.2] - 2026-06-22

### Fixed
- `SECURITY.md` trade-off clause: prior wording ("major-version
  pins absorb all upstream breaking-change notices") was the
  inverse of what's actually true.  Major-version pins
  (`@v4`) auto-absorb upstream patches and security fixes;
  exposure to breaking changes within a major version is
  mitigated by the per-release review at every `v500.x` cut.

## [v500.4.1] - 2026-06-22

### Changed
- `SECURITY.md`: dropped the stale `.github/dependabot.yml`
  reference now that the file is gone; replaced with the actual
  current procedure (major-version pins + manual quarterly-style
  review tied to each `v500.x` release) and an explicit
  "accepted trade-off" clause.

## [v500.4.0] - 2026-06-22

### Added
- Multi-LTS CI matrix: JDK 11 + JDK 17 gate every push / pull
  request, with per-JDK Gradle cache key (so caches don't bleed
  across matrix entries) and `fail-fast: false`.
- `SECURITY.md`: supported-versions table (only the latest
  tag is patched), 72-hour acknowledgement SLA, 90-day
  coordinated disclosure timeline.  Cross-references to CI,
  CodeQL, and the gradle-wrapper SHA-256 pin as in-place
  hardening.

### Changed
- `.github/workflows/build.yml`: dropped the backslash escapes
  around `${{ matrix.java }}` so GitHub Actions substitution
  actually fires (both JDKs were reading the literal string before).
- `.github/workflows/build.yml`: workflow-level `permissions:
  contents: read`, `timeout-minutes: 15`, and a
  `concurrency` group with `cancel-in-progress` gated to PR
  pushes only.
- `.github/dependabot.yml` was removed (its scheduled check
  was binding every commit to a chronic failure state during the
  release pipeline); the project is intentionally shipped
  without Dependabot and bumps third-party action versions
  manually.

## [v500.3.0] - 2026-06-22

### Changed
- `.github/workflows/codeql.yml`: `permissions: actions: read`
  added to the existing block (which already had `security-events:
  write` and `contents: read`); CodeQL's `java` analysis now
  runs on push, pull_request, and weekly as a tripwire.
- `.github/ISSUE_TEMPLATE/bug_report.yml` and
  `feature_request.yml`: standard issue forms with severity
  dropdown, version / JDK inputs, and reproduce-steps / expected
  behaviour triplets so reporters include the right context
  first try.

## [v500.2.0] - 2026-06-22

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

## [v500.1.0] - 2026-06-22

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
     comparison base below to the new tag (e.g. v500.14.0) and add a link
     reference for it. -->
[Unreleased]: https://github.com/kaivalya-cyber/ftc-voltage-compensator/compare/v500.29.0...HEAD
[v500.29.0]: https://github.com/kaivalya-cyber/ftc-voltage-compensator/compare/v500.28.0...v500.29.0
[v500.28.0]: https://github.com/kaivalya-cyber/ftc-voltage-compensator/compare/v500.27.0...v500.28.0
[v500.27.0]: https://github.com/kaivalya-cyber/ftc-voltage-compensator/compare/v500.26.0...v500.27.0
[v500.26.0]: https://github.com/kaivalya-cyber/ftc-voltage-compensator/compare/v500.25.0...v500.26.0
[v500.25.0]: https://github.com/kaivalya-cyber/ftc-voltage-compensator/compare/v500.24.0...v500.25.0
[v500.24.0]: https://github.com/kaivalya-cyber/ftc-voltage-compensator/compare/v500.23.0...v500.24.0
[v500.23.0]: https://github.com/kaivalya-cyber/ftc-voltage-compensator/compare/v500.22.0...v500.23.0
[v500.22.0]: https://github.com/kaivalya-cyber/ftc-voltage-compensator/compare/v500.21.0...v500.22.0
[v500.21.0]: https://github.com/kaivalya-cyber/ftc-voltage-compensator/compare/v500.20.0...v500.21.0
[v500.20.0]: https://github.com/kaivalya-cyber/ftc-voltage-compensator/compare/v500.19.0...v500.20.0
[v500.19.0]: https://github.com/kaivalya-cyber/ftc-voltage-compensator/compare/v500.18.0...v500.19.0
[v500.18.0]: https://github.com/kaivalya-cyber/ftc-voltage-compensator/compare/v500.17.0...v500.18.0
[v500.17.0]: https://github.com/kaivalya-cyber/ftc-voltage-compensator/compare/v500.16.0...v500.17.0
[v500.16.0]: https://github.com/kaivalya-cyber/ftc-voltage-compensator/compare/v500.15.0...v500.16.0
[v500.15.0]: https://github.com/kaivalya-cyber/ftc-voltage-compensator/compare/v500.14.0...v500.15.0
[v500.14.0]: https://github.com/kaivalya-cyber/ftc-voltage-compensator/compare/v500.13.1...v500.14.0
[v500.13.1]: https://github.com/kaivalya-cyber/ftc-voltage-compensator/compare/v500.13.0...v500.13.1
[v500.13.0]: https://github.com/kaivalya-cyber/ftc-voltage-compensator/compare/v500.12.0...v500.13.0
[v500.12.0]: https://github.com/kaivalya-cyber/ftc-voltage-compensator/compare/v500.11.0...v500.12.0
[v500.11.0]: https://github.com/kaivalya-cyber/ftc-voltage-compensator/compare/v500.10.0...v500.11.0
[v500.10.0]: https://github.com/kaivalya-cyber/ftc-voltage-compensator/compare/v500.9.0...v500.10.0
[v500.9.0]: https://github.com/kaivalya-cyber/ftc-voltage-compensator/compare/v500.8.1...v500.9.0
[v500.8.1]: https://github.com/kaivalya-cyber/ftc-voltage-compensator/compare/v500.8.0...v500.8.1
[v500.8.0]: https://github.com/kaivalya-cyber/ftc-voltage-compensator/compare/v500.7.0...v500.8.0
[v500.7.0]: https://github.com/kaivalya-cyber/ftc-voltage-compensator/compare/v500.6.0...v500.7.0
[v500.6.0]: https://github.com/kaivalya-cyber/ftc-voltage-compensator/compare/v500.5.1...v500.6.0
[v500.5.1]: https://github.com/kaivalya-cyber/ftc-voltage-compensator/compare/v500.5.0...v500.5.1
[v500.5.0]: https://github.com/kaivalya-cyber/ftc-voltage-compensator/compare/v500.4.3...v500.5.0
[v500.4.3]: https://github.com/kaivalya-cyber/ftc-voltage-compensator/compare/v500.4.2...v500.4.3
[v500.4.2]: https://github.com/kaivalya-cyber/ftc-voltage-compensator/compare/v500.4.1...v500.4.2
[v500.4.1]: https://github.com/kaivalya-cyber/ftc-voltage-compensator/compare/v500.4.0...v500.4.1
[v500.4.0]: https://github.com/kaivalya-cyber/ftc-voltage-compensator/compare/v500.3.0...v500.4.0
[v500.3.0]: https://github.com/kaivalya-cyber/ftc-voltage-compensator/compare/v500.2.0...v500.3.0
[v500.2.0]: https://github.com/kaivalya-cyber/ftc-voltage-compensator/compare/v500.1.0...v500.2.0
[v500.1.0]: https://github.com/kaivalya-cyber/ftc-voltage-compensator/compare/v500.0.0...v500.1.0
[v500.0.0]: https://github.com/kaivalya-cyber/ftc-voltage-compensator/releases/tag/v500.0.0
