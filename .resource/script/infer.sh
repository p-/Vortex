#!/bin/bash

set -ev

# Constants:
INFER_HOME="${HOME}/infer"
INFER_URL="https://github.com/facebook/infer/releases/download/v${INFER_VERSION}/infer-linux64-v${INFER_VERSION}.tar.xz"

# Make a folder for Infer:
ls -la ${HOME}
ls -la $INFER_HOME
mkdir -p $INFER_HOME

# Install Infer if not exists:
if [ ! -f "$INFER_HOME/bin/infer" ]; then
	# Download last Infer:
	curl -sSL $INFER_URL | tar -C $INFER_HOME -xJ

	# Move one level up:
	mv $INFER_HOME/infer-linux64-v$INFER_VERSION/* $INFER_HOME
	rm -fr $INFER_HOME/infer-linux64-v$INFER_VERSION
fi

ls -la $INFER_HOME

# Install Python v2.7 (required by Infer):
sudo apt-get install python2.7-minimal
python2.7 --version
