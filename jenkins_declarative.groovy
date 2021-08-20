pipeline {
  agent {
    kubernetes {
      yaml '''
              kind: Pod
              spec:
                containers:
                - name: kaniko
                  image: gcr.io/kaniko-project/executor:debug
                  imagePullPolicy: Always
                  command:
                  - sleep
                  args:
                  - 99d
                - name: ubuntu
                  image: ubuntu:20.04
                  imagePullPolicy: Always
                  command:
                  - sleep
                  args:
                  - 99d
        '''
    }
  }
  stages {
    stage ('GitClone'){
        steps {
          git branch: 'main', url: 'https://github.com/rpuserh/docker-test.git'
          sh 'ls'
        }
    }
    stage('BuildPush') {
      steps {
        container('kaniko') {
          sh '/kaniko/executor -f `pwd`/Dockerfile -c `pwd` --destination=252072232973.dkr.ecr.us-east-1.amazonaws.com/app:$BUILD_NUMBER'
        }
      }
    }
    stage('list') {
      steps {
        container('ubuntu') {
          sh 'pwd; ls -alih'
        }
      }
    }
  }
}
