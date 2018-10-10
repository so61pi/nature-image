# This unit is pulled in by basic.target

[Unit]
Description=Temporary Directory (/tmp)
ConditionPathIsDirectory=/tmp
ConditionDirectoryNotEmpty=!/tmp
Before=local-fs.target
After=swap.target

[Mount]
What=tmpfs
Where=/tmp
Type=tmpfs
Options=mode=1777,strictatime,nosuid,nodev,MACRO_SIZE

# Shadow stock tmp.mount
[Install]
WantedBy=local-fs.target
