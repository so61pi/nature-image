[Unit]
Description=Temporary Directory (/home)
ConditionPathIsDirectory=/home
ConditionDirectoryNotEmpty=!/home
Before=local-fs.target
After=swap.target

[Mount]
What=tmpfs
Where=/home
Type=tmpfs
Options=mode=0755,nosuid,nodev,MACRO_SIZE
