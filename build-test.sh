#!/bin/bash

chmod -R 777 *.sh

sed -i -e 's/\r$//' build.sh

echo "Pulling new source version!"
git pull
echo "Done!"

echo "Shut down all!"
./build.sh service-stack-down
echo "Done!"

echo "Start clean projects!"
./build.sh clean
echo "Done!"

echo "Start compile projects!"
./build.sh compile
echo "Done!"

echo "Start package projects!"
./build.sh package
echo "Done!"

echo "Start build docker!"
./build.sh docker-build
echo "Done!"

echo "Up!"
./build.sh service-stack-up
echo "Done!"
