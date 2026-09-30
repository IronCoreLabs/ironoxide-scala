# Releasing

- Make sure `main` has everything being released, a [CHANGELOG.md](./CHANGELOG.md) entry for the new version, and a row in the [README.md](./README.md) compatibility table.
- Run the [Bump Version](https://github.com/IronCoreLabs/ironoxide-scala/actions/workflows/bump-version.yaml) workflow with the new release version, e.g. `0.19.0`. It commits and tags the release, bumps [version.sbt](./version.sbt) to the next prerelease, and creates a GitHub release.
- The GitHub release triggers the [Publish](https://github.com/IronCoreLabs/ironoxide-scala/actions/workflows/publish.yaml) workflow, which publishes to [Maven Central](https://central.sonatype.com/artifact/com.ironcorelabs/ironoxide-scala_2.13). Watch its progress at https://central.sonatype.com/publishing/deployments; no action should be required.

Releases are signed with the IronCore Labs (Package Signing) subkey `9FA43559`, stored IronHide-encrypted in [.github/9FA43559.asc.iron](.github/9FA43559.asc.iron). It is the same key file as in [ironcore-alloy](https://github.com/IronCoreLabs/ironcore-alloy); to extend its expiration, follow alloy's [PGP.md](https://github.com/IronCoreLabs/ironcore-alloy/blob/main/.github/PGP.md) and copy the updated file here.
