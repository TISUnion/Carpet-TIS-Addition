**English** | [中文](development_cn.md)

\>\>\> [Back to index](readme.md)

# Development

Current main development branch: **1.15.2**

Current maintaining branches:
- 1.14.4, for Minecraft 1.14.4
- 1.15.2, for Minecraft 1.15.2
- 1.16.5, for Minecraft 1.16.4 to 1.16.5
- 1.17.1, for Minecraft 1.17.1
- 1.18.x, for Minecraft 1.18 experimental snapshots

Current archived branches:
- archive/1.16, for Minecraft 1.16 to 1.16.1
- archive/1.16.3, for Minecraft 1.16.2 to 1.16.3
- archive/1.17, for Minecraft 1.17

For general new features, implement them in 1.15.2 branch first then merge it into other branches

Branches merge order:
- 1.15.2 -> 1.14.4
- 1.15.2 -> 1.16.5 -> 1.17.1 -> 1.18.x
- 1.15.2 -> master (when release)

For version specific fixes / patches, implement them in relevant branches

master branches usually only receives doc updates directly

Try not to affect version compatibility unless it's necessary

The English doc and the Chinese doc are aligned line by line btw
