{
  inputs = {
    typelevel-nix.url = "github:typelevel/typelevel-nix";
    nixpkgs.follows = "typelevel-nix/nixpkgs";
    flake-utils.follows = "typelevel-nix/flake-utils";
  };

  outputs = { self, nixpkgs, flake-utils, typelevel-nix }:
    flake-utils.lib.eachDefaultSystem (system:
      let
        pkgs = import nixpkgs {
          inherit system;
          overlays = [ typelevel-nix.overlay ];
        };

        mkShell = jdk: pkgs.devshell.mkShell {
          imports = [ typelevel-nix.typelevelShell ];
          name = "ironoxide-scala";
          typelevelShell = {
            jdk.package = jdk;
          };
        };
      in
      rec {
        devShell = devShells."temurin@17";

        devShells = {
          "temurin@11" = mkShell pkgs.temurin-bin-11;
          "temurin@17" = mkShell pkgs.temurin-bin-17;
        };
      }
    );
}

