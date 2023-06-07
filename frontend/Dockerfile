FROM node:18.12.1-alpine AS build

# Set the working directory to /app inside the container
WORKDIR /app

# We copy the package definitions first, so we don't have to reinstall
# all the packages every time only a few other files change.
COPY package.json ./
COPY package-lock.json ./

# Install dependencies (npm ci makes sure the exact versions in the lockfile gets installed)
RUN npm ci --silent --only-production

# Now we copy the rest of the files
# This is done after installing dependencies to make sure the dependencies are cached
# and only the files that change are copied
COPY . ./

# Then we build the React code
RUN npm run build

##############################
# Production Stage
FROM nginx:1.21.0-alpine

COPY --from=build /app/build /usr/share/nginx/html

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]


## Now we "start over" with a fresh nginx image. In here, we will copy the build result
## from the previous section and set up the automatic backend loading
#FROM nginx
#
## Some configuration
#COPY nginx.conf /etc/nginx/nginx.conf
#
## Copy the code from the build result
#COPY --from=build /app/build /app
#
## Move the files from the build result to the template directory
#WORKDIR /app/static/js
#RUN mkdir /etc/nginx/templates; \
#    for file in *.js; \
#    do \
#        mv -- "$file" "/etc/nginx/templates/$file.template"; \
#    done