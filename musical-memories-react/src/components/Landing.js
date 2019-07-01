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
        console.log(this.state.lyric);
        if(this.state.lyric!==null)
        {
            $.ajax({
                url: '/song?lyric='+this.state.lyric,
                method: "POST",
                statusCode: {
                    200: () => {
                        console.log("success!")
                        $.ajax({
                            url: '/authorization',
                            method: "GET",
                            success: function (resp) {
                                console.log(resp)
                                window.location.href = resp;
                                return resp;
                            },
                        });
                    }
                }
            });
        }
    }

    render() {
        return (
            <div>
                <div className="jumbotron text-center">
                    <h1>ListenLater</h1>
                </div>
                <div className="row">
                    <div className="col-sm-4">
                    </div>
                    <div className="col-sm-4">
                        <form className="form-inline">
                            <div className="form-group">
                                <input type="text" className="form-control" id="lyric" placeholder="Lyric" onChange={this.handleLyricChange}/>
                            </div>
                            <button type="submit" id="submitBtn" onClick = {this.submit} className="btn btn-primary mb-2">submit</button>
                        </form>
                    </div>
                 </div>
            </div>
      ) ;
    }
}

export default withRouter(Landing);
