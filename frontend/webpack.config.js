module.exports = {
  // the main entry of our app
  entry: [
    './src/index.js', './src/auth/index.js'
  ],
  // output configuration
  output: {
    path: __dirname + '/build/',
    publicPath: 'build/',
    filename: 'build.js'
  },

  module: {
    loaders: [
      // process *.vue files using vue-loader
      {
        test: /\.vue$/,
        loader: 'vue'
      },
      // process *.js files using babel-loader
      // the exclude pattern is important so that we don't
      // apply babel transform to all the dependencies!
      {
        test: /\.js$/,
        loader: 'babel',
        exclude: /node_modules/
      }
    ]
  },

  babel: {
    presets: ['es2015'],
    plugins: ['transform-runtime']
  },

  devServer: {
    port: 8081,
    proxy: {
      "/api/*": "http://localhost:8080",
      "/oauth/token": "http://localhost:8080"
    },
    "changeOrigin": true,
    "secure": false,
    headers: {
      "Access-Control-Allow-Origin": "*",
      "Access-Control-Allow-Methods": "GET, POST, PUT, DELETE, PATCH, OPTIONS",
      "Access-Control-Allow-Headers": "X-Requested-With, content-type, Authorization"
    }
  }
}
