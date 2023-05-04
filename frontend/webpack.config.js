const Dotenv = require('dotenv-webpack');
const path = require('path');
module.exports = {
    resolve: {
      alias: {
        config: path.resolve(__dirname, 'config.js'),
      }
    },
    plugins: [
      new Dotenv()
    ],
    module: {
      rules: [
        {
          test: /\.js$/,
          exclude: /node_modules/,
          use: [{


            loader: "babel-loader"
          },
        ]
        },

        {
          test: /\.css$/,
          use : [
              {
                      loader: 'style-loader',
              },
              {
                      loader: 'css-loader',
                      options: {
                              sourceMap: true,
                      }
              }
          ]
        },
        {
          test: /\.(png|jp(e*)g|svg|gif)$/,
          type: "asset/resource",
        }
      ]
    }
  };
