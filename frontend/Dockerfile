FROM node:16.17.1 as NODE

WORKDIR /app
ENV PATH /app/node_modules/.bin:$PATH

COPY --chown=4001:275 package*.json ./

RUN npm i
COPY . .
# RUN apk add --no-cache bash
RUN mkdir node_modules/.cache
RUN chown -R 4001:275 .

CMD ["npm", "run", "prod-build"]