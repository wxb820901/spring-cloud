pipeline {
  agent any
  stages {
    stage('download') {
      steps {
        powershell(script: 'git clone https://github.com/wxb820901/spring-cloud.git;cd spring-cloud;', returnStdout: true, returnStatus: true, label: 'download')
        powershell(script: 'mvn clean install -pl demo-eureka dockerfile:build;mvn clean install -pl demo-config dockerfile:build', returnStatus: true, returnStdout: true, label: 'build')
      }
    }
  }
}