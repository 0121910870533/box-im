import App from './App'
import request from './common/request';
import emotion from './common/emotion.js';
import * as  enums from './common/enums.js';
import * as date from './common/date';
import * as socketApi from './common/wssocket';
import store from './store';

import { createSSRApp } from 'vue'

export function createApp() {
  const app = createSSRApp(App)
  app.use(store);
  app.config.globalProperties.$http = request;
  app.config.globalProperties.$wsApi = socketApi;
  app.config.globalProperties.$emo = emotion;
  app.config.globalProperties.$enums = enums;
  app.config.globalProperties.$date = date;
  return {
    app
  }
}
