# Contributing

Thanks for your interest in making `ftc-voltage-compensator` better.  This is
a small Java library that FTC teams drop into their `TeamCode/src/main/java`
tree, so contributions should keep it **dependency-free** and **easy to
vendor**.

## Quick start

```sh
# 1. Fork & clone
git clone https://github.com/<you>/ftc-voltage-compensator.git
cd ftc-voltage-compensator

# 2. Edit code, then run tests \u2014 either path works:

./runtests.sh                # plain javac/java (no Gradle needed)
./gradlew runUnitTests       # same suite, via the wrapper
```

Tests must end with `Tests: 40   Passed: 40   Failed: 0`.

Before tuning `VoltageCompensator` for your robot, read
[ARCHITECTURE.md](ARCHITECTURE.md) for the state-machine diagrams,
the per-loop interaction sequence, and the constants table with
rationale.

## Project layout

```
VoltageCompensator.java              # the library itself
VoltageCompensatedTeleOp.java       # reference OpMode wiring the library
test/                                # FTC SDK stubs + unit tests
  com/qualcomm/robotcore/...         # stub interfaces mirroring the real
  org/firstinspires/ftc/robotcore/...
  org/firstinspires/ftc/teamcode/util/VoltageCompensatorTest.java

.github/workflows/build.yml          # CI: JDK 17, javac, runs the suite
gradlew + gradle/wrapper/            # optional Gradle 9.6.0 wrapper
build.gradle + gradle.properties     # optional Gradle config (sources at
                                     # repo root, stubs under test/, tests
                                     # separated by include filter)
LICENSE                              # MIT
CHANGELOG.md                         # one entry per release
```

## Adding a new stub interface

Both `./runtests.sh` and `./gradlew classes / runUnitTests` use the stubs
under `test/com/...` and `test/org/...` as a compile-time shim for the FTC
SDK.  When `VoltageCompensator.java` or `VoltageCompensatedTeleOp.java`
references a new FTC SDK type, add a minimal interface at the matching path
under `test/` so the unit-test invocation still works.

- Mirror **only** the methods that the production sources actually call \u2014
  no need to cover the whole SDK surface.
- Place the file at the package its counterpart lives under in the real
  SDK (e.g. `test/com/qualcomm/robotcore/hardware/MyType.java` for
  `com.qualcomm.robotcore.hardware.MyType`).
- Copy the existing stubs as templates; each has a small Javadoc noting
  "compile-only."

## Coding style

- Java 11 source level works in CI, but the FTC SDK plugin runs at
  Java 8 at runtime.  Avoid Java 11+ syntax (records, sealed types,
  pattern matching) so production code is still consumable by an FTC
  robot Android project.  Sticking strictly to Java 8 source features
  is the safer rule for code destined to ship onto a robot.
- One public class per file.
- Constants grouped at the top with `// \u2500\u2500 Section header \u2500\u2500` comments.
- Public methods have a Javadoc; private helpers may omit it.
- Tests use a tiny in-source harness (`VoltageCompensatorTest.main(...)`) \u2014
  no JUnit, no TestNG, no external test framework.

## Pull requests

1. Push your branch to your fork on GitHub, then open a PR targeting
   `kaivalya-cyber/ftc-voltage-compensator:main`:
   ```sh
   git checkout -b my-change
   git commit -m "fix: ..."
   git push -u origin my-change
   # Open the PR via the GitHub UI on your fork's branch.
   ```
2. **One change per PR.**  Mixing refactors with new features makes review
   and rerolls harder.
3. Run `./runtests.sh` **and** `./gradlew runUnitTests` locally before
   opening the PR \u2014 both must report `Passed: 40`.
4. Reference any open issue with `Fixes #N` notation.
5. Add an entry to `CHANGELOG.md` under the next `[Unreleased]` heading
   (or under a tightly-scoped `[vX.Y.Z] - YYYY-MM-DD` if you tag the
   release yourself).
6. Don't introduce external dependencies (no Maven Central jars, no
   Android Gradle plugin, no Maven, no Gradle wrapper-shaking).  The
   library must remain drop-in for any FTC project.
7. **Before tagging a release**, run the local tag-changelog
   consistency check to make sure every `[vX.Y.Z]` CHANGELOG entry
   has a matching git tag and vice versa:
   ```sh
   CHANGELOG_TAGS=$(grep -oE '^## \[v[0-9]+\.[0-9]+\.[0-9]+\]' CHANGELOG.md | sed 's/^## \[//;s/\]//' | sort -V)
   GIT_TAGS=$(git tag -l 'v*.*.*' | sort -V)
   diff <(echo "$CHANGELOG_TAGS") <(echo "$GIT_TAGS") && echo 'OK'
   ```
   (We deliberately do **not** run this as a CI gate \u2014 multiple attempts
   to fetch all tags on GitHub Actions runners were unreliable.  The
   local check works every time.)

## What NOT to do

- Don't add dependencies.  This library intentionally bundles **zero**
  third-party jars so FTC teams can copy the .java files verbatim into a
  larger FTC project without worrying about classpath collisions.
- Don't rewrite the compensation math without a working test demonstrating
  the new behaviour.  Adding a `testXxx()` method to
  `VoltageCompensatorTest` is the easiest way.
- **Don't break the public API casually.**  Prefer additive changes and
  `@Deprecated` annotations over breaking changes.  Any breaking
  change requires a major-version bump (`vX.0.0`) AND a `### Removed`
  / `### Changed (BREAKING)` section in `CHANGELOG.md`.
- **Don't introduce a test framework (JUnit, TestNG, JUnit Jupiter,
  etc.).**  Keep the in-source `VoltageCompensatorTest.main()` harness
  so the suite runs with nothing more than a JDK.  New contributors
  reflexively `gradle init`-ing a JUnit setup is the most common
  accidental regression.
- **Don't add a new `@vN`-pinned `uses:` reference without expecting
  the `action-drift` workflow to flag it.**  The weekly action-drift
  workflow opens a single GitHub issue labeled `dependencies-drift`
  within ~7 days if your pin falls behind the upstream latest tag
  within the same major.  This is the deliberate replacement for
  `dependabot.yml` (which we deliberately do not run, see
  `SECURITY.md`); bumping the `@vN` portion is the expected
  follow-up.  Docker container actions (`uses: docker://image:tag`)
  are also enumerated in the same issue so they are never silently
  dropped from the scan.

## License

By contributing you agree that your contributions are licensed under the
project's [MIT](LICENSE) license.
