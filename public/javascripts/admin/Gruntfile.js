module.exports = function(grunt) {
  grunt.loadNpmTasks('grunt-requirejs');
  grunt.loadNpmTasks('grunt-contrib-copy');
  grunt.loadNpmTasks('grunt-s3');

  grunt.initConfig({
    pkg: grunt.file.readJSON('package.json'),

    requirejs: {
      dist: {
        options: {
          wrapShim: true,
          baseUrl: 'app/',
          mainConfigFile: 'app/config.js',
          out: 'dist/js/mealchooser-admin.js',
          include: ['requireLib', 'config', 'app'],
          insertRequire: ['app'],
          paths: {
            'handlebars': '../bower_components/handlebars/handlebars.runtime'
          }
        }
      }
    },

    copy: {
      dist: {
        files: [
          {
            expand: true,
            cwd: 'styles/',
            src: '**',
            dest: 'dist/css/'
          },
          {
            expand: true,
            cwd: 'fonts/',
            src: '**',
            dest: 'dist/fonts/'
          }
        ]
      }
    },

    aws: grunt.file.readJSON("aws.json"),

    s3: {
      options: {
        key: "<%= aws.key %>",
        secret: "<%= aws.secret %>",
        bucket: 'mealchooser-admin',
        region: 'eu-west-1',
        access: 'public-read'
      },

      uploadBuild: {
        upload: [
          {
            src: 'dist/**/*',
            rel: 'dist/',
            dest: 'builds/',
          }
        ]
      }
    }
  });

  grunt.registerTask('build', ['copy:dist', 'requirejs']);
  grunt.registerTask('deploy', ['build', 's3:uploadBuild']);
}