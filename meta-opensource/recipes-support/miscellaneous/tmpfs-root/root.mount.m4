[Unit]
Description=Temporary Directory (/root)
ConditionPathIsDirectory=/root
ConditionDirectoryNotEmpty=!/root
Before=local-fs.target
After=swap.target

[Mount]
What=tmpfs
Where=/root
Type=tmpfs
Options=mode=0755,nosuid,nodev,MACRO_SIZE
