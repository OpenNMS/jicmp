Releasing a New Version
-----------------------

1. edit [pom.xml.in](./pom.xml.in) and remove `-SNAPSHOT` from the version tag
2. edit [ChangeLog](./ChangeLog) and [debian/changelog](./debian/changelog) and update to include the changes
3. `git commit -a -m 'JICMP X.X.X'`
4. `git tag -s jicmp-X.X.X-1`
5. `git push origin jicmp-X.X.X-1`
6. edit [pom.xml.in](./pom.xml.in) and re-add `-SNAPSHOT` to the version tag
7. edit [version.m4](./version.m4) and increment the version
8. `git commit -a -m 'X.X.X -> X.X.Y-SNAPSHOT'`
9. `git push`
