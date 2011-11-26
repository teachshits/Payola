package s2js.compiler

class MethodSpecs extends CompilerFixtureSpec {
    describe("Method calls") {
        it("default arguments are supported") {
            configMap =>
                expect {
                    """
                        object o1 {
                            def m1(v1:String, v2:String = "") {}
                            def m2(v1:String, v2:String = null) {}
                            def m3() {
                                m1("foo")
                                m1("foo", "bar")
                                m2("foo")
                            }
                        }
                    """
                } toBe {
                    """
                        goog.provide('o1');

                        o1 = {};
                        o1.m1 = function(v1, v2) {
                            var self = this;
                            if (typeof(v2) === 'undefined') { v2 = ''; };
                        };
                        o1.m2 = function(v1, v2) {
                            var self = this;
                            if (typeof(v2) === 'undefined') { v2 = null; };
                        };
                        o1.m3 = function() {
                            var self = this;
                            self.m1('foo');
                            self.m1('foo', 'bar');
                            self.m2('foo');
                        };
                    """
                }
        }

        it("generic methods can be called") {
            configMap =>
                expect {
                    """
                        object a {
                            def m1[T](t: T) {}
                            def m2() {
                                m1("foo")
                            }
                        }
                    """
                } toBe {
                    """
                        goog.provide('a');

                        a = {};
                        a.m1 = function(t) {
                            var self = this;
                        };
                        a.m2 = function() {
                            var self = this;
                            self.m1('foo');
                        };
                    """
                }
        }

        it("methods from same package should be fully qualified") {
            configMap =>
                expect {
                    """
                        package s2js.adapters.goog

                        object a {
                            val x = css.getCssName("foo")
                        }
                    """
                } toBe {
                    """
                        goog.provide('goog.a');
                        goog.require('goog.css');

                        goog.a = {};
                        goog.a.x = goog.css.getCssName('foo');
                    """
                }
        }

         it("can call a method of returned object") {
            configMap =>
                expect {
                    """
                        class A {
                            def go(x:String) = "foo" + x
                        }

                        object b {
                            def m1(): A = new A
                            def m2() {
                                val x = m1().go("bar").toString
                            }
                        }

                    """
                } toBe {
                    """
                        goog.provide('A');
                        goog.provide('b');

                        A = function() {
                            var self = this;
                        };
                        A.prototype.go = function(x) {
                            var self = this;
                            return ('foo' + x);
                        };

                        b = {};
                        b.m1 = function() {
                            var self = this;
                            return new A();
                        };
                        b.m2 = function() {
                            var self = this;
                            var x = self.m1().go('bar').toString();
                        };
                    """
                }
        }

        ignore("can have multiple parameter lists") {
            configMap =>
                expect {
                """
                    import s2js.adapters.js.browser._

                    object o1 {
                        def m1(name:String)(fn: (String) => Unit) {
                            fn(name)
                        }
                        def m3() {
                            m1("foo") {
                                x => window.alert(x)
                            }
                        }
                    }
                """
                } toBe {
                """
                    goog.provide('o1');

                    o1 = {};
                    o1.m1 = function(name, fn) {
                        var self = this;
                        fn(name);
                    };

                    o1.m3 = function() {
                        var self = this;
                        o1.m1('foo', function(x) {window.alert(x);});
                    };
                """
                }
        }


        ignore("support methods as parameters") {
            configMap =>
                expect {
                    """
                        import s2js.adapters.js.browser._

                        class C1 {
                            val f1 = "c1"
                            def m1(fn: (String) => Unit) {
                                window.alert(f1)
                                fn(f1)
                            }
                        }

                        class C2 {
                            val f1 = "c2"
                            def m1(v1: String) {
                                window.alert(v1 + f1)
                            }
                        }

                        object o1 {
                            def m1() {
                                val c1 = new C1
                                val c2 = new C2
                                c1.m1(c2.m1)
                            }
                        }
                    """
                } toBe {
                    """
                        goog.provide('C1');
                        goog.provide('C2');
                        goog.provide('o1');

                        C1 = function() {
                            var self = this;
                            self.f1 = 'c1';
                        };
                        C1.prototype.m1 = function(fn) {
                            var self = this;
                            window.alert(self.f1);
                            fn(self.f1);
                        };

                        C2 = function() {
                            var self = this;
                            self.f1 = 'c2';
                        };
                        C2.prototype.m1 = function(v1) {
                            var self = this;
                            console.log((v1 + self.f1));
                        };

                        o1 = {};
                        o1.m1 = function() {
                            var self = this;
                            var c1 = new C1();
                            var c2 = new C2();
                            c1.m1(c2.m1);
                        };
                    """
                }
        }
    }
}