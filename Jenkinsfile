pipeline {
  agent any
  stages {

    stage('Test') {
      when { changeRequest() }
      agent {
        docker {
          image 'maven:3.9.0-eclipse-temurin-19'
          args '-v /root/.m2:/root/.m2'
        }
      }
      steps {
        dir('app') {
          sh "mvn test"
          // archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
        }
      }
    }

    stage('Linter') {
      when { changeRequest() }
      agent {
        docker {
          image 'maven:3.9.0-eclipse-temurin-19'
          args '-v /root/.m2:/root/.m2'
        }
      }
      steps {
        dir('app') {
          sh "mvn checkstyle:check"
          archiveArtifacts 'target/checkstyle-result.xml'
        }
      }
    }

    stage('Build Docker image') {
      when { not { changeRequest() }}
      steps {
        script {
          // Build the image using Dockerfile
          def image = docker.build("felpsmac/rancher-fleet-poc", "./app")
          // Push image to the registry
          docker.withRegistry('https://registry.hub.docker.com', 'docker-hub-credentials') {
            image.push("${GIT_COMMIT}")
            image.push("latest")
          }
        }
      }
    }

    stage('Download yq') {
      when {
        not {
          anyOf {
            changeRequest();
            expression { return fileExists ('k8s/base/yq') }
          }
        }
      }
      steps {
        dir('k8s/base') {
          sh "curl -LJO https://github.com/mikefarah/yq/releases/latest/download/yq_linux_amd64"
          sh "mv yq_linux_amd64 yq && chmod +x yq"
        }
      }
    }

    stage('Update app manifest') {
      when { not { changeRequest() }}
      steps {
        dir('k8s/base') {
          sh "./yq -i \'.images[0].newTag = \"${GIT_COMMIT}\"\' kustomization.yml"
          sh 'cat kustomization.yml'
        }
      }
    }

    stage('Commit updated manifest') {
      when { not { changeRequest() }}
      steps {
        script {
          sh 'git add k8s/base/kustomization.yml'
          sh "git commit -m 'Update app version to ${GIT_COMMIT} [skip ci]'"

          withCredentials([usernamePassword(credentialsId: 'github', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
            sh "git push https://${USERNAME}:${PASSWORD}@github.com/MrChampz/rancher-fleet-poc.git HEAD:main"
          }
        }
      }
    }
  }

  post {
    // Clean after build
    always {
      cleanWs()
    }
  }
}

