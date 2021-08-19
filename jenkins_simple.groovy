podTemplate(yaml: '''
              kind: Pod
              spec:
                containers:
                - name: kaniko
                  image: gcr.io/kaniko-project/executor:v1.6.0-debug
                  imagePullPolicy: Always
                  command:
                  - sleep
                  args:
                  - 99d
'''
  ) {

  node(POD_LABEL) {
    stage('Build with Kaniko') {
      git branch: 'main', url: 'https://github.com/rpuserh/docker-test.git'
      container('kaniko') {
        sh 'mkdir /kaniko/.docker || true'
        sh 'echo \'{ "credsStore": "ecr-login" }\' > /kaniko/.docker/config.json'
        //sh '/kaniko/executor -f `pwd`/Dockerfile -c `pwd` --no-push'
        sh '/kaniko/executor -f `pwd`/Dockerfile -c `pwd` --destination=252072232973.dkr.ecr.us-east-1.amazonaws.com/app:$BUILD_NUMBER'
      }
    }
  }
}
