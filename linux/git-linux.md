apt-get install git
git --version

git config --global user.name "wangcunxin"
git config --global user.email "wangcx1217@163.com"
git config --list

# checkout a project
git init git_repository
git clone https://github.com/wangcunxin/spark-py.git


#create a new repository on the command line

echo "# spark_py" >> README.md
git init
git add README.md
git commit -m "first commit"
git remote add origin https://github.com/wangcunxin/spark_py.git
git push -u origin master

//update
git pull origin develop
//delete
git checkout -D develop
//rebuild
git checkout -b develop
//merge from master to develop
git merge master 
