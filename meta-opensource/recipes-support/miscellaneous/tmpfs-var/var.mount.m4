# This unit is pulled in by basic.target using RequiresMountsFor

[Unit]
Description=Temporary Directory (/var)
ConditionPathIsDirectory=/var
ConditionDirectoryNotEmpty=!/var
Before=local-fs.target
After=swap.target

[Mount]
What=tmpfs
Where=/var
Type=tmpfs
Options=mode=0777,strictatime,nosuid,nodev,MACRO_SIZE
