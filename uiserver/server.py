from flask import Flask
from flask import request, session
from flask import redirect, url_for, render_template

app = Flask(__name__)
app.secret_key = b'bruh'

boards = []


@app.route('/show_board', methods=['GET', 'POST'])
def show_board():
    if 'current' not in session or ('current' in session and session['current'] > (len(boards) -1)):
        session['current'] = 0

    if 'next' in request.form and session['current'] < (len(boards) - 1):
        session['current'] = session['current'] + 1
    elif 'prev' in request.form and session['current'] > 0:
        session['current'] = session['current'] - 1

    auto_turn = True if 'auto-turn' in request.form and request.form['auto-turn'] == 'on' else False

    return render_template('board.html', board=boards[session['current']], auto_turn=auto_turn)


@app.route('/post_board', methods=['POST', 'GET'])
def update_board():
    if request.method == 'POST':
        boards.append(request.get_json(silent=True))
        # print(request.get_json(silent=True), flush=True)
    return str(len(boards))


@app.route('/')
def index():
    return redirect(url_for('show_board'))


if __name__ == '__main__':
    app.run()
