import random
import string

from flask import Flask, jsonify, make_response, request, Response
from werkzeug.security import generate_password_hash, check_password_hash
# from flask_sqlalchemy import SQLAlchemy

from functools import wraps
import uuid
import jwt
import datetime

from werkzeug.utils import secure_filename

from db import db, db_init
from db import Users, UsersAds, Advertisements, Picture

salt = 'lkshgiewrivcn94785498_'

app = Flask(__name__)

app.config['SECRET_KEY'] = '004f2af45d3a4e161a7dd2d17fdae47f'
app.config['SQLALCHEMY_DATABASE_URI'] = \
    'sqlite://///home/Michael0832/mysite/rentapp.db'
app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = True

db_init(app)


def token_required(f):
    @wraps(f)
    def decorator(*args, **kwargs):
        token = None
        if 'x-access-tokens' in request.headers:
            token = request.headers['x-access-tokens']

        if not token:
            return jsonify({'message': 'a valid token is missing'})
        try:
            data = jwt.decode(token, app.config['SECRET_KEY'], algorithms=["HS256"])
            current_user = Users.query.filter_by(tel=data['public_id']).first()
        except:
            return jsonify({'message': 'token is invalid'})

        return f(current_user, *args, **kwargs)

    return decorator


# generating random symbol sequence, just to fill 'tel' field in Users
def id_generator(size=6, chars=string.ascii_uppercase + string.digits):
    return ''.join(random.choice(chars) for _ in range(size))


# {
#     "tel": "0993366580",
#     "password": "asdf",
#     "pib": "OMN",
#     "description": "Testing new database...",
#     "location": "Kharkiv",
#     "is_remote_viewer": true
# }


@app.route('/register', methods=['POST'])
def signup_user():
    data = request.get_json()
    hashed_password = generate_password_hash(str(data['password']) + salt, method='sha256')

    # photo_file = request.files['profile_image']
    # if not photo_file:
    #     return 'No photo uploaded', 400
    # photo_file_name = secure_filename(photo_file.filename)
    # photo_file_mimetype = photo_file.mimetype
    # photo_file_uuid = uuid.uuid4()
    #
    # picture = Picture(uuid=photo_file_uuid,
    #                   raw=photo_file.read(),
    #                   name=photo_file_name,
    #                   mimetype=photo_file_mimetype)
    #
    # db.session.add(picture)
    # db.session.commit()
    # #
    new_user = Users(tel=data['tel'], pib=data['pib'],
                     password=hashed_password,
                     description=data['description'], location=data['location'],
                     is_remote_viewer=data['is_remote_viewer'],
                     photo_file=None)

    db.session.add(new_user)
    db.session.commit()
    return jsonify({'message': 'registered successfully'})


@app.route('/login', methods=['POST'])
def login_user():
    # auth = request.authorization
    # if not auth or not auth.username or not auth.password:
    data = request.get_json()
    if not data:
        return make_response('could not verify', 401, {'Authentication': 'login required"'})

    user = Users.query.filter_by(tel=data['tel']).first()
    if not user:
        return 'Query returned empty result'

    if check_password_hash(user.password, data['password'] + salt):
        token = jwt.encode(
            {'public_id': user.tel, 'exp': datetime.datetime.utcnow() + datetime.timedelta(minutes=45)},
            app.config['SECRET_KEY'], "HS256")

        return jsonify({'token': token})

    return make_response('could not verify', 401, {'Authentication': '"login required"'})


@app.route('/users', methods=['GET'])
def get_all_users():
    users = Users.query.all()
    result = []
    for user in users:
        user_data = {}
        user_data['tel'] = user.tel
        user_data['pib'] = user.pib
        user_data['password'] = user.password
        user_data['photo_file'] = user.photo_file
        user_data['description'] = user.description
        user_data['location'] = user.location
        user_data['is_remote_viewer'] = user.is_remote_viewer

        result.append(user_data)

    return jsonify({'users': result})


@app.route('/user')
@token_required
def get_itself(user):
    user_data = {}
    user_data['tel'] = user.tel
    user_data['pib'] = user.pib
    user_data['password'] = user.password
    user_data['photo_file'] = user.photo_file
    user_data['description'] = user.description
    user_data['location'] = user.location
    user_data['is_remote_viewer'] = user.is_remote_viewer

    return jsonify(user_data)



# @app.route('/advs', methods=['GET'])
# @token_required
# def get_ads(current_user):
#     advs = db.session.query(Advertisements).filter(
#         Advertisements.id.in_(
#             db.session.query(UsersAds.adv_id).filter(
#                 UsersAds.user_id == current_user.id
#             ).all()
#         )
#     ).all()
#         #UsersAds.query.filter_by(user_id=current_user.id).all()
#     # advs = db.session.query(Advertisements) \
#     #     .filter(Advertisements.id.in_(adv_ids)).all()
#
#     output = []
#     for adv in advs:
#         adv_data = {'id': adv.id,
#                     'photo_file': adv.photo_file,
#                     'tags': adv.tags, 'description': adv.description,
#                     'location': adv.location, 'size': adv.size,
#                     'room_count': adv.room_count, 'price': adv.price}
#
#         output.append(adv_data)
#
#     return jsonify({'List of your advertisements': output})


@app.route('/pic/<UID>')
def retreive_image(UID):
    image = Picture.query.filter_by(uuid=str(UID)).first()
    if not image:
        return 'Picture not found with id: ' + str(UID), 404
    return Response(image.raw, mimetype=image.mimetype)


@app.route('/account/pic', methods=['POST'])
@token_required
def add_pic_to_acc(current_user):
    # user = Users.query.filter_by(id=current_user.id).first()

    photo_file = request.files['profile_image']
    if not photo_file:
        return 'No photo uploaded', 400
    photo_file_name = secure_filename(photo_file.filename)
    photo_file_mimetype = photo_file.mimetype
    photo_file_uuid = str(uuid.uuid4())

    picture = Picture(uuid=photo_file_uuid,
                      raw=photo_file.read(),
                      name=photo_file_name,
                      mimetype=photo_file_mimetype)

    db.session.add(picture)
    db.session.commit()

    current_user.photo_file = photo_file_uuid
    db.session.commit()
    return photo_file_uuid, 200


@app.route('/account', methods=['PUT'])
@token_required
def edit_acc(current_user):
    data = request.get_json()
    # hashed_password = generate_password_hash(str(data['password']) + salt, method='sha256')
    # current_user.tel = data['tel']

    if 'pib' in data:
        current_user.pib = data['pib']
    if 'description' in data:
        current_user.description = data['description']
    if 'location' in data:
        current_user.location = data['location']
    if 'is_remote_viewer' in data:
        current_user.is_remote_viewer = data['is_remote_viewer']

    db.session.commit()
    return 'account successfully edited', 200


@app.route('/adv', methods=['POST'])
@token_required
def adv_add(current_user):
    data = request.get_json()
    advertisement = Advertisements(photo_file=None, tags=data['tags'],
                                   description=data['description'], location=data['location'],
                                   size=data['size'], room_count=data['room_count'],
                                   price=data['price'])

    db.session.add(advertisement)
    db.session.flush()  # now, id is not None

    adv_usr = UsersAds(user_id=current_user.id, adv_id=advertisement.id,
                       creation_date=datetime.datetime.utcnow())

    db.session.add(adv_usr)
    db.session.commit()

    return str(advertisement.id), 200


@app.route('/adv/<int:id>', methods=['PUT'])
@token_required
def edit_advertisement(current_user, id):
    data = request.get_json()
    adv = Advertisements.query.get(id)

    if 'tags' in data:
        adv.tags = data['tags']
    if 'description' in data:
        adv.description = data['description']
    if 'location' in data:
        adv.location = data['location']
    if 'size' in data:
        adv.size = data['size']
    if 'room_count' in data:
        adv.room_count = data['room_count']
    if 'price' in data:
        adv.price = data['price']

    db.session.flush()
    ret_id = adv.id
    db.session.commit()
    return str(ret_id), 200


@app.route('/adv/<int:id>', methods=['DELETE'])
@token_required
def delete_advertisement(current_user, id):
    adv = Advertisements.query.get(id)
    db.session.delete(adv)
    db.session.commit()
    return 'Advertisement deleted successfully', 200


@app.route('/advs')
@token_required
def select_advertisements(current_user):
    # result = db_session.query(table_1).filter(table_1.id.in_((1,2,3,5))).all()

    # adv_ids = db.session.query(UsersAds).filter(
    #     UsersAds.user_id == current_user.id
    # ).all()
    #ids = list(map(lambda x: x.adv_id, adv_ids))
    # for id in adv_ids:
    #     ids.append(id.adv_id)

    ads = db.session.query(Advertisements).filter(
        Advertisements.id.in_(
            list(
                map(lambda x: x.adv_id,
                    db.session.query(UsersAds.adv_id).filter(
                    UsersAds.user_id == current_user.id
                    ).all()
                )
            )
        )
    ).all()

    result = []
    for ad in ads:
        ad_data = {}
        ad_data['id'] = ad.id
        ad_data['tags'] = ad.tags
        ad_data['description'] = ad.description
        ad_data['location'] = ad.location
        ad_data['size'] = ad.size
        ad_data['room_count'] = ad.room_count
        ad_data['price'] = ad.price
        result.append(ad_data)

    return jsonify({"List of your ads": result})


@app.route('/adv/<int:id>/pic', methods=['POST'])
@token_required
def add_pic_to_adv(current_user, id):
    adv = Advertisements.query.get(id)

    photo_file = request.files['advertisement_image']
    if not photo_file:
        return 'No photo uploaded', 400
    photo_file_name = secure_filename(photo_file.filename)
    photo_file_mimetype = photo_file.mimetype
    photo_file_uuid = str(uuid.uuid4())

    picture = Picture(uuid=photo_file_uuid,
                      raw=photo_file.read(),
                      name=photo_file_name,
                      mimetype=photo_file_mimetype)

    db.session.add(picture)
    db.session.commit()

    adv.photo_file = photo_file_uuid
    db.session.commit()

    return photo_file_uuid, 200


@app.route('/remoters')
def get_remoters():
    users = Users.query.filter_by(is_remote_viewer=True).all()
    result = []
    for usr in users:
        usr_data = {}
        usr_data['pib'] = usr.pib
        usr_data['tel'] = usr.tel
        usr_data['description'] = usr.description
        usr_data['location'] = usr.location

        result.append(usr_data)

    return jsonify({"List of remoters available": result})


@app.route('/')
def index():
    return '<h1>Hello!</h1>'


if __name__ == "__main__":
    app.run(debug=True)
