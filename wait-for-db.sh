HOST="localhost"
PORT="4444"
USER="root"
PASSWORD="$DB_PASSWORD"

until echo '\q' | mysql -h"$HOST" -P"$PORT" -u"$USER" -p"$PASSWORD"; do
    >&2 echo "MySQL is unavailable - sleeping"
    sleep 1
done

>&2 echo "MySQL and Data are up - executing command"
