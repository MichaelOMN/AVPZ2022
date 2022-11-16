from flask_sqlalchemy import SQLAlchemy

db = SQLAlchemy()

class Users(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    pib = db.Column(db.String(100), nullable=False)
    tel = db.Column(db.String(20), unique=True, nullable=False)
    password = db.Column(db.String(50), nullable=False)
    photo_file = db.Column(db.String(50), db.ForeignKey('picture.uuid'), nullable=True)
    description = db.Column(db.Text, nullable=False)
    location = db.Column(db.Text, nullable=False)
    is_remote_viewer = db.Column(db.Boolean)

class Advertisements(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    photo_file = db.Column(db.String(50), db.ForeignKey('picture.uuid'), nullable=True)
    tags = db.Column(db.String(100), nullable=False)
    description = db.Column(db.Text, nullable=False)
    location = db.Column(db.Text, nullable=False)
    size = db.Column(db.FLOAT, nullable=False)
    room_count = db.Column(db.Integer, nullable=False)
    price = db.Column(db.Integer, nullable=True)

class UsersAds(db.Model):
    entry_id = db.Column(db.Integer, primary_key=True)
    user_id = db.Column(db.Integer, db.ForeignKey('users.id'), nullable=False)
    adv_id = db.Column(db.Integer, db.ForeignKey('advertisements.id'), nullable=False)
    creation_date = db.Column(db.DateTime, nullable=False)

class Picture(db.Model):
    uuid = db.Column(db.String(50), primary_key=True)
    raw = db.Column(db.LargeBinary, unique=True, nullable=False)
    name = db.Column(db.Text, nullable=False)
    mimetype = db.Column(db.Text, nullable=False)


#INSERT INTO Picture(uuid, raw, name, mimetype)
#VALUES('sdklfjfgyoieu234!!.2ab', readfile('man.webp'), 'man', 'image/webp')

#INSERT INTO Users(id, pib, tel, mimetype)
#VALUES('sdklfjfgyoieu234!!.2ab', readfile('man.webp'), 'man', 'image/webp')

def db_init(app):
    db.init_app(app)
    with app.app_context():
        db.create_all()