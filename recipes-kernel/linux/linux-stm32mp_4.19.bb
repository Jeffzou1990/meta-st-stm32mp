SUMMARY = "Linux STM32MP Kernel"
SECTION = "kernel"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=bbea815ee2795b2f4230826c0c6b8814"

include linux-stm32mp.inc

#LINUX_VERSION = "4.19"

PV = "4.19.49+git${SRCPV}"
#PV = "${LINUX_VERSION}"

S = "${WORKDIR}/linux-4.19.49"

# ---------------------------------
# Configure devupstream class usage
# ---------------------------------
#BBCLASSEXTEND = "devupstream:target"

SRC_URI = "git://github.com/STMicroelectronics/linux.git;protocol=https;branch=v${LINUX_VERSION}-stm32mp;name=linux"
SRCREV = "9cc80ff80f5ea5f1ff35122f61afaa7b11ad22ae"
#SRCREV_FORMAT_class-devupstream = "linux"
#PV_class-devupstream = "${LINUX_VERSION}+github+${SRCPV}"

# ---------------------------------
# Configure default preference to manage dynamic selection between tarball and github
# ---------------------------------
#STM32MP_SOURCE_SELECTION ?= "tarball"

#DEFAULT_PREFERENCE = "${@bb.utils.contains('STM32MP_SOURCE_SELECTION', 'github', '-1', '1', d)}"

# ---------------------------------
# Configure archiver use
# ---------------------------------
include ${@oe.utils.ifelse(d.getVar('ST_ARCHIVER_ENABLE') == '1', 'linux-stm32mp-archiver.inc','')}

# -------------------------------------------------------------
# Defconfig
#
KERNEL_DEFCONFIG        = "multi_v7_defconfig"
KERNEL_CONFIG_FRAGMENTS = "${@bb.utils.contains('KERNEL_DEFCONFIG', 'multi_v7_defconfig', '${S}/arch/arm/configs/fragment-01-multiv7_cleanup.config', '', d)}"
KERNEL_CONFIG_FRAGMENTS += "${@bb.utils.contains('KERNEL_DEFCONFIG', 'multi_v7_defconfig', '${S}/arch/arm/configs/fragment-02-multiv7_addons.config', '', d)}"
KERNEL_CONFIG_FRAGMENTS += "${@bb.utils.contains('DISTRO_FEATURES', 'systemd', '${WORKDIR}/fragments/4.19/fragment-03-systemd.config', '', d)} "
KERNEL_CONFIG_FRAGMENTS += "${@bb.utils.contains('COMBINED_FEATURES', 'optee', '${WORKDIR}/fragments/4.19/fragment-04-optee.config', '', d)}"
KERNEL_CONFIG_FRAGMENTS += "${WORKDIR}/fragments/4.19/fragment-05-modules.config"

SRC_URI += "file://4.19/fragment-03-systemd.config;subdir=fragments"
SRC_URI += "file://4.19/fragment-04-optee.config;subdir=fragments"
SRC_URI += "file://4.19/fragment-05-modules.config;subdir=fragments"


# -------------------------------------------------------------
# Kernel Args
#
KERNEL_EXTRA_ARGS += "LOADADDR=${ST_KERNEL_LOADADDR}"
