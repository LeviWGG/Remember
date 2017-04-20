#!/bin/bash

git pull
source build/envsetup.sh
lunch snt132-userdebug
make update-api
if [ ! $? -eq 0 ]; then
    echo "make error"
    exit -1
fi

lunch sdk_snt132-userdebug
make win_sdk

if [ ! $? -eq 0 ]; then
    echo "make sdk error"
    exit -1
fi

win_path=out/host/windows/sdk/sdk_snt132
lin_path=out/host/linux-x86/sdk/sdk_snt132
rm -rf ${lin_path}/android-sdk_eng.Data.BU_linux-x86/tools
rm -rf ${win_path}/android-sdk_eng.Data.BU_windows/tools
unzip vendor/forge/tools/tools-win.zip -d ${win_path}/android-sdk_eng.Data.BU_windows
unzip vendor/forge/tools/tools-linux.zip -d ${lin_path}/android-sdk_eng.Data.BU_linux-x86
my_path=$PWD

cd ${win_path}
zip -q -r sdk-win.zip android-sdk_eng.Data.BU_windows
cd ${my_path}

cd ${lin_path}
zip -q -r sdk-linux.zip android-sdk_eng.Data.BU_linux-x86
cd ${my_path}

rm -rf ${lin_path}/android-sdk_eng.Data.BU_linux-x86/tools
unzip vendor/forge/tools/tools-mac.zip -d ${lin_path}/android-sdk_eng.Data.BU_linux-x86
cd ${lin_path}
zip -q -r sdk-mac.zip android-sdk_eng.Data.BU_linux-x86
cd ${my_path}

rm -rf out/host/sdk
mkdir out/host/sdk
mv ${lin_path}/sdk-linux.zip ${lin_path}/sdk-mac.zip out/host/sdk/
mv ${win_path}/sdk-win.zip out/host/sdk/ 
cd out/host/sdk
md5sum sdk-win.zip sdk-linux.zip sdk-mac.zip > sdk_md5.txt
cd ${my_path}