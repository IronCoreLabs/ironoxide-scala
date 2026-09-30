# Releasing

We publish to [Maven Central](https://central.sonatype.com/artifact/com.ironcorelabs/ironoxide-scala_2.13) with `sbt release`, which tests, sets the version, tags, signs, uploads through the Sonatype Central Portal, and bumps to the next `-SNAPSHOT`.

## Before releasing

- Make sure `main` has everything being released, a [CHANGELOG.md](./CHANGELOG.md) entry for the new version, and a row in the [README.md](./README.md) compatibility table.
- Download the `libironoxide_java` (`.dylib` on macOS, `.so` on Linux) matching the `ironoxide-java` version in [build.sbt](./build.sbt) from [ironoxide-swig-bindings releases](https://github.com/IronCoreLabs/ironoxide-swig-bindings/releases) into the repo root. The release runs the tests, which load it from there.
- Login to [Sonatype Central](https://central.sonatype.com/) with the username `icl-devops` and the password stored in `IT_Info/sonatype-info.txt.iron`. From the username menu go to your profile, click `Generate User Token`, and export the token:

  ```sh
  export SONATYPE_USERNAME=<token username>
  export SONATYPE_PASSWORD=<token password>
  ```

- Import the signing key. In Google Drive, navigate to the `IT_Info/pgp` folder, download `rsa-signing-subkey.asc.iron` and `ops-info.txt.iron`, and decrypt them using IronHide. Then:
  1. Copy the master password from `ops-info.txt` to your clipboard so it can be provided when importing the secret key.
  2. `gpg --keyserver keys.openpgp.org --receive-keys 62F57B1B87928CAC`
  3. `gpg --import rsa-signing-subkey.asc`

## Release

`main` only accepts squash-merged PRs, so the release runs on its own branch.

1. `git switch main && git pull && git switch -c release-vX.Y.Z && git push -u origin release-vX.Y.Z`
2. `sbt release`, accepting the default release and next versions. It pushes the version commits to the branch and the `vX.Y.Z` tag to GitHub.
3. Open a PR from `release-vX.Y.Z` and squash-merge it. The tag stays on the branch's `Setting version to X.Y.Z` commit, which is the published source.

If `sbt release` fails before `sonatypeBundleRelease` finishes, nothing was published. Delete the local tag with `git tag -d vX.Y.Z`, reset the branch with `git reset --hard origin/release-vX.Y.Z`, fix the problem, and run it again.
