# Security Policy

This document explains which versions of `ftc-voltage-compensator` receive
security patches, how to report a vulnerability, and our disclosure
timeline.

## Supported Versions

| Tag range         | Status          | Patched through |
|-------------------|-----------------|-----------------|
| `v500.x.x` (latest) | **Active**    | Latest tag      |
| `v500.0.0` (initial) | Historical | Upgrade to latest |
| anything older    | Not patched     | N/A             |

Only the latest tagged release and the `main` branch receive security
patches.  Older releases are kept in the tag history for reproducibility
but are **NOT** patched; please upgrade.

## Reporting a Vulnerability

**Please do not** open a public GitHub issue for unpatched security
problems in this repository.  Public issues leak pre-patch vulnerability
details to attackers.

Send reports privately to the maintainer via a GitHub security advisory
(preferred):

* [Open a private security advisory](https://github.com/kaivalya-cyber/ftc-voltage-compensator/security/advisories/new)

Include in the report:

* A clear description of the vulnerability and its real-world impact on
  an FTC robot (can it cause crashes? wrong compensator outputs that
  pass safety checks?)
* Reproduction steps or a test/fixture that triggers it reliably
* The affected version (commit SHA, tag, or branch)
* What you would consider a reasonable fix timeline

We aim to acknowledge within **72 hours** of receipt.

## Disclosure

We follow a **90-day coordinated disclosure** policy:

* **Day 0**: report received, ack sent
* **Within 14 days**: triage complete and a fix proposal drafted
* **Within ~45 days**: PR with fix + regression test ready for review
* **Within 90 days**: patch released on `main`, tagged as a new
  `v500.x` release, a `### Security` entry added to `CHANGELOG.md`,
  GitHub advisory published (CVE assigned if applicable)

After public disclosure, we credit the reporter in the release notes
unless they prefer to stay anonymous.

## Hardening guarantees already in place

* Every push / pull request runs the full `./runtests.sh` and
  `./gradlew runUnitTests` suite on **both JDK 17 and JDK 11** (matrix).
  All 40 unit tests must pass before merging.
* CodeQL's `java` analysis runs on every push, every pull request, and
  weekly as a tripwire.  SARIF findings surface on the Security tab.
* All third-party GitHub Actions are referenced by major-version
  tags (`@v4`, `@v3`); no in-repo `.github/dependabot.yml` is shipped,
  and GitHub Actions versions are reviewed and bumped manually in the
  workflow files at each `v500.x` release.  Major-version pins
  auto-absorb upstream patches and security fixes; any breaking
  change within a major version is caught at the per-bump review.
* The Gradle wrapper distribution (`gradle-9.6.0-bin.zip`) is pinned by
  SHA-256 in `gradle/wrapper/gradle-wrapper.properties`; any tampering
  fails the wrapper at first run.

Thanks for helping keep FTC teams' batteries safer.
