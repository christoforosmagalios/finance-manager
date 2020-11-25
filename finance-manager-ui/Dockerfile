#############################################################################################
###                                  Build Application                                    ###
#############################################################################################
FROM node:12.7-alpine AS build
WORKDIR /usr/src/app
COPY package.json package-lock.json ./
RUN npm install
RUN npm install -g @angular/cli@9.0.0
COPY . .
RUN ng build --prod

#############################################################################################
###                                     Run Application                                   ###
#############################################################################################
FROM nginx:1.19.1-alpine
RUN rm -rf /usr/share/nginx/html/* && rm -rf /etc/nginx/conf.d/default.conf
COPY ./nginx.conf /etc/nginx/conf.d/default.conf
COPY --from=build /usr/src/app/dist/finance-manager-ui /usr/share/nginx/html
