#!/usr/bin/env bash

# regenerates the liquibase diff, which needs to be manually added to the liquibase changelog

XML_DIFF="target/db_changelog_diff.xml"
XML_LOG="src/main/resources/db/changelog/db.changelog-master.xml"

echo
echo "$ mvn clean compile liquibase:update liquibase:diff"
echo

mvn clean compile liquibase:update liquibase:diff
MVN_CMD_RETURNCODE=$?

if [ ${MVN_CMD_RETURNCODE} -ne 0 ]; then
    echo
    echo "Maven execution failed with error code $MVN_CMD_RETURNCODE"
    echo
    exit 1
fi

echo
echo "Done creating liquibase diff XML file."
echo

OS=`uname`
if [[ "$OS" == 'Darwin' ]]; then
    open ${XML_DIFF}
    open ${XML_LOG}
else
    cat $XML_DIFF
    echo "Open [$XML_DIFF] and cut the nodes under the root."
    echo "Then paste those nodes at the end of [$XML_LOG]."
fi
