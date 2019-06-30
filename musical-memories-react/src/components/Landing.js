'use strict';

import React, {Component} from 'react';
import {Link, withRouter} from 'react-router-dom';
import $ from 'jquery';


class Landing extends Component {
    constructor(props) {
        super(props);
        this.state = {lyric: ''};
        this.submit = this.submit.bind(this);
        this.handleLyricChange = this.handleLyricChange.bind(this);
    }

    handleLyricChange(event) {
        const lyric = event.target.value;
        if (lyric!==null) {
            this.setState({
                lyric: lyric
            });
        }
    }

    submit(event) {
        event.preventDefault();
        if(this.state.lyric!==null)
        {
            $.ajax({
                url: '/v1/lyric?q='+this.state.lyric,
                method: "POST",
                statusCode: {
                    200: () => {
                        console.log("success!")
                    }
                }
            });
        }
    }

    render() {
        return (<div>
            <div className="jumbotron"></div>
            <div className="container">
                <div className ="row">
                    <div className=".col-sm-6">
                    <form className="form-horizontal">
                        <input className="form-control" type="text" name="lyric" id="lyric" placeholder="Lyric"
                               onChange={this.handleLyricChange}/>
                        <button onClick={this.submit} id="submitBtn" type="submit"
                                className="btn btn-primary"><span className="glyphicon glyphicon-ok"></span>
                        </button>
                    </form>
                    </div>
                </div>
            </div>
        </div>);
    }
}

export default withRouter(Landing);