SUMMARY = "Rns Mavlink Services"
DESCRIPTION = "Provides RNS Mavlink flight controller and ground control services. This recipe installs a pre-build release."

SECTION = "reticulum"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=f978e2caad0e533cf3b63ddb6d8dec6f"

SRC_URI = "https://github.com/BeechatNetworkSystemsLtd/rns-mavlink-rs/releases/download/v0.1.0/arm64-debug.tar.gz;"
SRC_URI[sha256sum] = "27b4318e11962a24f86ea1d8d9e92d1eabf656782902d490d671815aa865f9d4"

# unpacked tar files
S = "${WORKDIR}/dist-arm64-debug"

inherit systemd

# Systemd
SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE:${PN} = "rns-mavlink-fc.service rns-mavlink-gc.service"
SYSTEMD_AUTO_ENABLE:${PN} = "enable"

# skip license check
do_populate_lic[noexec] = "1"

do_install:append() {
    # working directory will contain configs
    install -d ${D}/home/root/rns-mavlink
    # copy configs
    install ${B}/Fc.toml ${D}/home/root/rns-mavlink
    install ${B}/Gc.toml ${D}/home/root/rns-mavlink

    # /usr/bin
    install -d ${D}${bindir}
    # copy binaries
    install -m 0755 ${B}/rns-mavlink-fc ${D}${bindir}
    install -m 0755 ${B}/rns-mavlink-gc ${D}${bindir}

    # /usr/lib/systemd/system
    install -d ${D}${systemd_system_unitdir}
    # copy services
    install -m 0644 ${B}/rns-mavlink-fc.service ${D}${systemd_system_unitdir}
    install -m 0644 ${B}/rns-mavlink-gc.service ${D}${systemd_system_unitdir}
}

# Files
FILES:${PN} += " \
    ${systemd_system_unitdir}/rns-mavlink-fc.service \
    ${systemd_system_unitdir}/rns-mavlink-gc.service \
    /home/root/rns-mavlink/Fc.toml \
    /home/root/rns-mavlink/Gc.toml \
"
# runtime dependencies
RDEPENDS:${PN} += "systemd"
