NativeGap
=========

It shows how to use native footer and header with Phonegap and JS


The idea behind the project is to preserve all the business logic in js (which can be reused on mobile platforms like iOS/Android/BB/WP8)
while using native controls. This simplifies things and allows better performaces.

For instance, it's possible to hook a js handler to a button in an header or a footer (for the android version the binding/unbinding is done through the view id).
The native button is only in charge of forwarding its native click event to the javascript logic.
