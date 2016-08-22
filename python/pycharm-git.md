#pycharm

vcs->checkout->git
vcs->import ->create git repository
git->add->commit
git->push

command line:
rm -rf gewa-spark
git clone git@192.168.2.34:matrixgroup/gewa-spark.git
cd gewa-spark/
ls
git chekout develop
git status
git pull origin status

git add .
git commit -m "fix kafka offset bug"
git push origin develop